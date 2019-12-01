package com.morningbees.service.token

import com.morningbees.config.TokenConfig
import com.morningbees.exception.ErrorCode
import com.morningbees.exception.UnAuthorizeException
import io.jsonwebtoken.*
import org.springframework.context.annotation.Configuration
import java.util.*
import kotlin.collections.HashMap



@Configuration
open class TokenService() {

    final val tokenConfig: TokenConfig = TokenConfig()

    private val SALT = tokenConfig.salt
    private val JWT_ALG = "HS567"
    private val JWT_TYPE = "JWT"

    open fun getExpirationDate(): Long { return Date().getTime() }
    open fun decodeAndGetInfos(JWTToken: String): AuthTokenInfos { return AuthTokenInfos() }

    fun encodeJWT(payload: HashMap<String, Any?>): String {
        val headers = HashMap<String, Any?>()
        headers.put("alg", JWT_ALG)
        headers.put("typ", JWT_TYPE)

        val claim = HashMap<String, Any?>()
        claim.put("exp", getExpirationDate())
        claim.put("sub", "morningbees")
        claim.put("iat", Date().time/1000)
        payload.forEach{ k,v -> claim.put(k, v) }

        val jwt = Jwts.builder()
                .setHeader(headers)
                .setClaims(claim)
                .signWith(SignatureAlgorithm.HS256, this.generateKey())
                .compact()
        return jwt
    }

    fun decodeJWT(JWTToken: String): Claims {
        try {
            val body :Claims = Jwts.parser()
                    .setSigningKey(this.generateKey())
                    .parseClaimsJws(JWTToken)
                    .body

            return body
        } catch (e: JwtException) {
            throw UnAuthorizeException(e.message.toString(), ErrorCode.InvalidAccessToken, "JWT_PARSE_ERROR")
        }
    }

    fun getTokenType(type: TokenType): Int {
        return when(type) {
            TokenType.ACCESS_TOKEN -> 0
            TokenType.REFRESH_TOKEN -> 1
        }
    }

    private fun generateKey(): kotlin.ByteArray {
        val key: kotlin.ByteArray = SALT.toByteArray()

        return key
    }
}

enum class TokenType { ACCESS_TOKEN, REFRESH_TOKEN }