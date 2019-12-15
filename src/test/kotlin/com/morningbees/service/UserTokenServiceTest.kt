package com.morningbees.service

import com.morningbees.SpringMockMvcTestSupport
import com.morningbees.model.User
import com.morningbees.repository.UserRepository
import com.morningbees.repository.UserTokenRepository
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired

internal class UserTokenServiceTest : SpringMockMvcTestSupport() {

    @Autowired
    lateinit var userTokenService: UserTokenService

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userTokenRepository: UserTokenRepository

    @BeforeEach
    fun setUp() {
        val user = User("test")
        userRepository.save(user)
    }

    @Test
    @DisplayName("UserToken이 신규로 생성된다.")
    @FlywayTest
    fun successCreateUserToken() {
        val user = userRepository.findById(1).get()

        assertNull(userTokenRepository.findByUser(user))

        userTokenService.createUserToken(user, "fcmToken", "refreshToken")

        assertNotNull(userTokenRepository.findByUser(user))
    }

    @Test
    @DisplayName("UserToken이 이미 있을경우 업데이트한다.")
    @FlywayTest
    fun successUpdateUserToken() {
        val user = userRepository.findById(1).get()
        userTokenService.createUserToken(user, "fcmToken", "beforeRefreshToken")
        assertEquals("beforeRefreshToken", userTokenRepository.findByUser(user)?.refreshToken)

        userTokenService.createUserToken(user, "fcmToken", "afterRefreshToken")
        assertEquals("afterRefreshToken", userTokenRepository.findByUser(user)?.refreshToken)
    }

    @Test
    @DisplayName("파라미터로 null이 넘어올 경우에는 업데이트하지 않는다.")
    @FlywayTest
    fun successNotUpdateIsNull() {
        val user = userRepository.findById(1).get()
        userTokenService.createUserToken(user, "beforeFcmToken", "RefreshToken")
        assertEquals("RefreshToken", userTokenRepository.findByUser(user)?.refreshToken)

        userTokenService.createUserToken(user, "afterFcmToken", null)
        assertEquals("RefreshToken", userTokenRepository.findByUser(user)?.refreshToken)
        assertEquals("afterFcmToken", userTokenRepository.findByUser(user)?.fcmToken)
    }
}