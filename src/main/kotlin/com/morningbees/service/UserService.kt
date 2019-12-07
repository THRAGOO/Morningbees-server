package com.morningbees.service

import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.User
import com.morningbees.model.UserProvider
import com.morningbees.repository.UserProviderRepository
import com.morningbees.repository.UserRepository
import com.morningbees.util.LogEvent
import com.sun.istack.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var userProviderRepository: UserProviderRepository

    fun signUpWithProvider(@NotNull email: String, @NotNull nickname: String, @NotNull provider: String): User {
        // 이미 가입된 이메일인지 확인 필요
        if ( userProviderRepository.existsByEmailAndProvider(email, provider) == true ) {
            throw BadRequestException("alreay exists social email", ErrorCode.AlreadySocialEmail, LogEvent.UserServiceProcessError.code)
        }
        if ( userRepository.existsByNickname(nickname) == true ) {
            throw BadRequestException("alreay exists nickname", ErrorCode.AlreadyNickname, LogEvent.UserServiceProcessError.code)
        }

        val user = User(nickname =  nickname)
        userRepository.save(user)

        val user_provider = UserProvider(user = user, email = email, provider = provider)
        userProviderRepository.save(user_provider)

        return user
    }

    fun getUserByEmail(@NotNull email: String): User? {
        val userProvider: UserProvider? = userProviderRepository.findByEmail(email)

        return userProvider?.user
    }

    fun isExistsNickname(nickname: String): Boolean {
        if ( userRepository.existsByNickname(nickname) == true ) {
            return false
        }

        return true
    }
}