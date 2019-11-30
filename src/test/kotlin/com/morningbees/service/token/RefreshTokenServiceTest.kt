package com.morningbees.service.token

import com.morningbees.SpringMockMvcTestSupport
import com.morningbees.exception.UnAuthorizeException
import com.morningbees.model.User
import com.morningbees.repository.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.util.*

internal class RefreshTokenServiceTest: SpringMockMvcTestSupport() {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var refreshTokenService: RefreshTokenService

    @BeforeEach
    fun setUp() {
        userRepository.save(User(nickname = "Test", status = 0))
    }

    @Test
    @FlywayTest
    fun generate() {
        val user : User? = userRepository.findByIdOrNull(1)
        val accessToken :String = refreshTokenService.generate(user!!)
        val body: Claims = Jwts.parser().setSigningKey("secret".toByteArray()).parseClaimsJws(accessToken).body

        assertEquals("Test", body["nickname"])
        assertEquals(1, body["userId"])
        assertEquals(refreshTokenService.getTokenType(TokenType.REFRESH_TOKEN), body["tokenType"])
    }

    @Test
    @FlywayTest
    @DisplayName("success decode token")
    fun successDecodeToken() {
        val user : User = userRepository.findById(1).orElse(null)
        val accessToken :String = refreshTokenService.generate(user)
        val tokenInfos: AuthTokenInfos = refreshTokenService.decodeAndGetInfos(accessToken)

        assertEquals("Test", tokenInfos.nickname)
        assertEquals(1, tokenInfos.userId)
        assertEquals(refreshTokenService.getTokenType(TokenType.REFRESH_TOKEN), tokenInfos.tokenType)
    }

    @Test
    @FlywayTest
    @DisplayName("fail decode token")
    fun failDecodeToken() {
        val headers = HashMap<String, Any?>()
        headers.put("alg", "HS256")
        headers.put("typ", "JWT")

        val claim = HashMap<String, Any?>()
        claim.put("exp", Date().getTime() + 1000)
        claim.put("iat", Date().time/1000)

        val accessToken :String = Jwts.builder()
                .setHeader(headers)
                .setClaims(claim)
                .signWith(SignatureAlgorithm.HS256, "unexpect_secret".toByteArray())
                .compact()
        assertThrows(UnAuthorizeException::class.java, { refreshTokenService.decodeAndGetInfos(accessToken) })
    }
}