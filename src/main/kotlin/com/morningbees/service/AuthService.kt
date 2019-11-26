package com.morningbees.service

import com.morningbees.model.User
import com.morningbees.repository.UserRepository
import com.morningbees.service.token.AccessTokenService
import com.sun.istack.NotNull
import org.springframework.beans.factory.annotation.Autowired

class AuthService {
    @Autowired
    lateinit var userRepository: UserRepository
    lateinit var accessTokenService: AccessTokenService

    fun signUp(@NotNull nickname: String): HashMap<String, String> {
        val user = User(nickname =  nickname)
        userRepository.save(user)

        val accessToken = accessTokenService.generate(user)
//        val refreshToken = tokenService.generateToken<Any>()

        val tokenStore = HashMap<String, String>()

        return tokenStore
    }
}