package com.morningbees.model

import com.morningbees.SpringMockMvcTestSupport
import com.morningbees.repository.UserProviderRepository
import com.morningbees.repository.UserRepository
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.lang.Exception

internal class UserProviderTest : SpringMockMvcTestSupport() {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userProviderRepository: UserProviderRepository

    @BeforeEach
    fun setUp() {
        val user = User("Test")
        userRepository.save(user)

        val userProvider = UserProvider(user, "naver", "test@naver.com")
        userProviderRepository.save(userProvider)
    }

    @Test
    @FlywayTest
    @DisplayName("UserProvider 생성을 성공하다.")
    fun successCreateUserProvider() {
        val userProvider: UserProvider = userProviderRepository.findById(1).get()

        assertEquals("naver", userProvider.provider)
        assertEquals("Test", userProvider.user.nickname)
    }

    @Test
    @FlywayTest
    @DisplayName("디비에 이미 등록된 provider와 email일 경우 에러를 발생한다.")
    fun failDuplicateProviderAndEmail() {
        val user = User("Test1")
        userRepository.save(user)

        val duplicatedUserProvider = UserProvider(user, "naver", "test@naver.com")
        assertThrows(Exception::class.java, { userProviderRepository.save(duplicatedUserProvider) })
    }
}