package com.morningbees.service.token

import com.morningbees.SpringMockMvcTestSupport
import com.morningbees.model.User
import com.morningbees.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull

internal class AccessTokenServiceTest: SpringMockMvcTestSupport() {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var accessTokenService: AccessTokenService

    @BeforeEach
    fun setUp() {
        userRepository.save(User(nickname = "Test", status = 0))
    }

    @Test
    @DisplayName("generate Access Token")
    fun generate() {
        var accessToken :String = ""

        val user :User? = userRepository.findByIdOrNull(1)

        if (user != null) {
            accessToken = accessTokenService.generate(user)
        }

        System.out.println(accessToken)
    }
}