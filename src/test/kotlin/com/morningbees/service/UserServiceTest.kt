package com.morningbees.service

import com.morningbees.SpringMockMvcTestSupport
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.UnAuthorizeException
import com.morningbees.model.User
import com.morningbees.model.UserProvider
import com.morningbees.repository.UserProviderRepository
import com.morningbees.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class UserServiceTest : SpringMockMvcTestSupport() {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userProviderRepository: UserProviderRepository

    @Test
    @DisplayName("유저 생성이 성공하다.")
    fun successSignUpWithProvider() {
        val user: User = userService.signUpWithProvider("test1@naver.com", "test1", "naver")

        assertEquals("test1", user.nickname)
    }

    @Test
    @DisplayName("같은 소셜 계정으로 이미 가입된 유저가 있을 때 에러가 리턴된다.")
    fun failBeacuseAlreadyExistsEmail() {
        val user: User = userRepository.save(User("test2"))
        userProviderRepository.save(UserProvider(user,"naver","test2@naver.com"))

        val thrown = assertThrows(BadRequestException::class.java) { userService.signUpWithProvider("test2@naver.com", "test2", "naver") }
        assertEquals(thrown.message, "alreay exists social email")
    }

    @Test
    @DisplayName("같은 닉네임으로 이미 가입된 유저가 있을 때 에러가 리턴된다.")
    fun failBeacuseAlreadyExistsNickname() {
        userRepository.save(User("test3"))
        val thrown = assertThrows(BadRequestException::class.java) { userService.signUpWithProvider("test3@naver.com", "test3", "naver") }

        assertEquals(thrown.message, "alreay exists nikcname")
    }
}