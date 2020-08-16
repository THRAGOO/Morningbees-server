package com.morningbees.service

import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.User
import com.morningbees.service.token.AccessTokenService
import com.morningbees.service.token.RefreshTokenService
import com.morningbees.util.LogEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthService {
    private val logger = org.slf4j.LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    lateinit var accessTokenService: AccessTokenService
    @Autowired
    lateinit var refreshTokenService: RefreshTokenService

    fun getAuthTokens(user: User?): HashMap<String, Any> {
        if ( user == null ) {
            throw BadRequestException("user is nil", ErrorCode.BadRequest, LogEvent.AuthServiceProcessError.code, logger)
        }

        val accessToken: String = accessTokenService.generate(user)
        val refreshToken: String = refreshTokenService.generate(user)

        val tokenStore = HashMap<String, Any>()
        tokenStore["accessToken"] = accessToken
        tokenStore["refreshToken"] = refreshToken

        return tokenStore
    }

}