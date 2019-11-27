package com.morningbees.service.token

import com.morningbees.exception.UnAuthorizeException
import io.jsonwebtoken.*
import java.util.*
import kotlin.collections.HashMap

open class TokenService {

    private val SALT = "secret"

    open fun getExpirationDate(): Long { return Date().getTime() }
    open fun decodeAndGetInfos(JWTToken: String): AuthTokenInfos { return AuthTokenInfos() }

    fun encodeJWT(payload: HashMap<String, Any?>): String {
        val headers = HashMap<String, Any?>()
        headers.put("alg", "HS256")
        headers.put("typ", "JWT")

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
            throw UnAuthorizeException(e.message.toString(), 101, "JWT_PARSE_ERROR")
        }
    }

    fun getTokenType(type: TokenType): Int {
        return when(type) {
            TokenType.ACCESS_TOKEN -> 0
            TokenType.REFRESH_TOKEN -> 1
        }
    }

    private fun generateKey(): ByteArray? {
        val key: ByteArray? = SALT.toByteArray()

        return key
    }
}

enum class TokenType { ACCESS_TOKEN, REFRESH_TOKEN }