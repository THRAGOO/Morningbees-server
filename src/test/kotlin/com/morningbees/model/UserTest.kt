package com.morningbees.model

import com.morningbees.SpringMockMvcTestSupport
import com.morningbees.repository.UserProviderRepository
import com.morningbees.repository.UserRepository
import com.morningbees.repository.UserTokenRepository
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.lang.Exception
import kotlin.test.assertEquals

internal class UserTest : SpringMockMvcTestSupport() {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userProviderRepository: UserProviderRepository

    @Autowired
    lateinit var userTokenRepository: UserTokenRepository

    @BeforeEach
    fun setUp() {
        val user = User("Test")
        userRepository.save(user)

        val userProvider = UserProvider(user, "naver", "test@maver.com")
        userProviderRepository.save(userProvider)

        val userToken = UserToken(user, "fcmToken", "refreshToken")
        userTokenRepository.save(userToken)
    }

    @Test
    @FlywayTest
    @DisplayName("UserProvider, UserToken 을 가진 User 를 생성한다.")
    fun successCreateUser() {
        val user = userRepository.findById(1).get()

        assertEquals("Test", user.nickname)
        assertEquals("naver", user.provider?.provider)
        assertEquals("fcmToken", user.token?.fcmToken)
    }

    @Test
    @FlywayTest
    @DisplayName("중복된 닉네임을 가진 유저가 있을경우 에러를 반환한다.")
    fun failDuplicateNickname() {
        val duplicateUser = User("Test")
        assertThrows(Exception::class.java, { userRepository.save(duplicateUser) })
    }
}