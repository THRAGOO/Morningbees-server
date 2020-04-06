package com.morningbees.service.token

import com.morningbees.model.User
import io.jsonwebtoken.Claims
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap

@Service
class AccessTokenService : TokenService() {

//    private val accessTokenExpireAt = 86400000 // 24시간
    private val accessTokenExpireAt = 300 // 24시간

    override fun getExpirationDate(): Long {
        val now = Date()
        val expiredTime :Int = accessTokenExpireAt

        return (now.time/1000) + expiredTime
    }

    fun generate(user: User): String {
        val additionalInfos = HashMap<String, Any?>()
        additionalInfos.put("userId", user.id)
        additionalInfos.put("nickname", user.nickname)
        additionalInfos.put("tokenType", this.getTokenType(TokenType.ACCESS_TOKEN))
        val jwt :String = encodeJWT(additionalInfos)

        return jwt
    }

    override fun decodeAndGetInfos(JWTToken: String): AuthTokenInfos {
        val body: Claims = this.decodeJWT(JWTToken)

        val userId: Long = (body["userId"] as Int).toLong()
        val tokenType: Int = body["tokenType"] as Int
        val nickname: String = body["nickname"].toString()
        val provider: String = body["provider"].toString()
        val authTokenInfos = AuthTokenInfos(userId, nickname, provider, tokenType)

        return authTokenInfos
    }
}