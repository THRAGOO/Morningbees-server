package com.morningbees.service.token

import com.morningbees.model.User
import io.jsonwebtoken.Claims
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap

@Service
class RefreshTokenService : TokenService() {

    private val refreshTokenExpireAt = 15552000 // 6개월

    override fun getExpirationDate(): Long {
        val now = Date()
        val expiredTime :Int = refreshTokenExpireAt

        return (now.time/1000) + expiredTime
    }

    fun generate(user: User): String {
        val additionalInfos = HashMap<String, Any?>()
        additionalInfos["userId"] = user.id
        additionalInfos["nickname"] = user.nickname
        additionalInfos["tokenType"] = this.getTokenType(TokenType.REFRESH_TOKEN)

        return encodeJWT(additionalInfos)
    }

    override fun decodeAndGetInfos(JWTToken: String): AuthTokenInfos {
        val body: Claims = this.decodeJWT(JWTToken)

        val userId: Long = (body["userId"] as Int).toLong()
        val tokenType: Int = body["tokenType"] as Int
        val nickname: String = body["nickname"].toString()
        val provider: String = body["provider"].toString()

        return AuthTokenInfos(userId, nickname, provider, tokenType)
    }
}