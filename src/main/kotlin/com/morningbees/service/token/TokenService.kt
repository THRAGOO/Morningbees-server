package com.morningbees.service.token

import com.morningbees.config.TokenConfig
import com.morningbees.exception.ErrorCode
import com.morningbees.exception.UnAuthorizeException
import com.morningbees.util.LogEvent
import io.jsonwebtoken.*
import org.springframework.context.annotation.Configuration
import java.util.*
import kotlin.collections.HashMap



@Configuration
class TokenService {

    private final val tokenConfig: TokenConfig = TokenConfig()

    private val SALT = tokenConfig.salt
    private val JWT_ALG = "HS567"
    private val JWT_TYPE = "JWT"

    fun getExpirationDate(): Long { return Date().time
    }
    fun decodeAndGetInfos(JWTToken: String): AuthTokenInfos { return AuthTokenInfos() }

    fun encodeJWT(payload: HashMap<String, Any?>): String {
        val headers = HashMap<String, Any?>()
        headers["alg"] = JWT_ALG
        headers["typ"] = JWT_TYPE

        val claim = HashMap<String, Any?>()
        claim["exp"] = getExpirationDate()
        claim["sub"] = "morningbees"
        claim["iat"] = Date().time/1000
        payload.forEach{ (k, v) -> claim[k] = v }

        return Jwts.builder()
                .setHeader(headers)
                .setClaims(claim)
                .signWith(SignatureAlgorithm.HS256, generateKey())
                .compact()
    }

    fun decodeJWT(JWTToken: String): Claims {
        try {

            return Jwts.parser()
                    .setSigningKey(generateKey())
                    .parseClaimsJws(JWTToken)
                    .body
        } catch (e: JwtException) {
            throw UnAuthorizeException(e.message.toString(), ErrorCode.InvalidAccessToken, LogEvent.TokenServiceProcessError.code)
        }
    }

    fun getTokenType(type: TokenType): Int {
        return when(type) {
            TokenType.ACCESS_TOKEN -> 0
            TokenType.REFRESH_TOKEN -> 1
        }
    }

    private fun generateKey(): ByteArray {

        return SALT.toByteArray()
    }
}

enum class TokenType { ACCESS_TOKEN, REFRESH_TOKEN }