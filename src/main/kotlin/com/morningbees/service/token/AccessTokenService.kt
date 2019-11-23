package com.morningbees.service.token

import com.morningbees.model.User
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.util.*
import kotlin.collections.HashMap

@Service
class AccessTokenService : TokenService() {

    private val accessTokenExpireAt = 60001

    override fun getExpirationDate(): Long {
        val now = Date()
        val expiredTime :Int = 1000 * accessTokenExpireAt

        return (now.time/1000) + expiredTime
    }

    fun generate(user: User): String {
        val userInfos = HashMap<String, Any?>()
        userInfos.put("userId", user.id)
        userInfos.put("userNickname", user.nickname)
        val jwt :String = generateJWTToken(userInfos)

        return jwt
    }
}