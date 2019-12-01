package com.morningbees.service

import com.morningbees.SpringMockMvcTestSupport
import com.morningbees.exception.BadRequestException
import com.morningbees.model.User
import com.morningbees.repository.UserRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired

internal class AuthServiceTest : SpringMockMvcTestSupport() {
    @Autowired
    lateinit var authService: AuthService

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    @DisplayName("유저 생성이 성공하다.")
    fun successGetAuthTokens() {
        val user = User(nickname =  "Test")
        userRepository.save(user)

        val tokens: HashMap<String, String> = authService.getAuthTokens(user)

        assertNotNull(tokens["accessToken"])
        assertNotNull(tokens["refreshToken"])
    }

    @Test
    @DisplayName("파라미터로 전달 받은 유저가 없을 때 에러가 발생한다.")
    fun failGetAuthTokens() {
        val thrown = assertThrows(BadRequestException::class.java) { authService.getAuthTokens(null) }
        assertEquals(thrown.message, "user is nil")
    }
}