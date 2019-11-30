package com.morningbees.service

import com.morningbees.SpringMockMvcTestSupport
import com.morningbees.model.User
import com.morningbees.model.UserProvider
import com.morningbees.repository.UserProviderRepository
import com.morningbees.repository.UserRepository
import com.morningbees.service.social.NaverLoginService
import com.morningbees.service.token.AccessTokenService
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull

internal class AuthServiceTest : SpringMockMvcTestSupport() {
    @Autowired
    lateinit var authService: AuthService

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userProviderRepository: UserProviderRepository

    @Test
    @FlywayTest
    @DisplayName("유저 생성이 성공하다.")
    fun successCreateUserSignUp() {
        authService.signUp("test@naver.com", "Test", "socialToken", "naver")

        val user: User? = userRepository.findByIdOrNull(1)
        assertEquals("Test", user?.nickname)
        val userProvider: UserProvider = userProviderRepository.findByUser(user!!)
        assertEquals("test@naver.com", userProvider.email)
    }
}