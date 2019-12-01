package com.morningbees.service

import com.morningbees.controller.ErrorController
import com.morningbees.exception.BadRequestException
import com.morningbees.model.User
import com.morningbees.model.UserProvider
import com.morningbees.repository.UserProviderRepository
import com.morningbees.repository.UserRepository
import com.morningbees.service.social.NaverLoginService
import com.morningbees.service.token.AccessTokenService
import com.morningbees.service.token.RefreshTokenService
import com.sun.istack.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthService {
    private val log = org.slf4j.LoggerFactory.getLogger(AuthService::class.java)


    @Autowired
    lateinit var accessTokenService: AccessTokenService
    @Autowired
    lateinit var refreshTokenService: RefreshTokenService

    fun getAuthTokens(user: User?): HashMap<String, String> {
        if ( user == null ) {
            throw BadRequestException("user is nil", 101, "")
        }

        val accessToken: String = accessTokenService.generate(user)
        val refreshToken: String = refreshTokenService.generate(user)

        val tokenStore = HashMap<String, String>()
        tokenStore.put("accessToken", accessToken)
        tokenStore.put("refreshToken", refreshToken)

        return tokenStore
    }

}