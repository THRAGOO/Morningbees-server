package com.morningbees.service

import com.morningbees.dto.meInfoDto
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.User
import com.morningbees.model.UserProvider
import com.morningbees.repository.UserProviderRepository
import com.morningbees.repository.UserRepository
import com.morningbees.util.LogEvent
import com.sun.istack.NotNull
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
        private val userRepository: UserRepository,
        private val userProviderRepository: UserProviderRepository
) {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    fun findById(userId: Long): User =
            userRepository.findByIdOrNull(userId)
                    ?: throw BadRequestException("user cannot found", ErrorCode.BadRequest, LogEvent.MissionVoteServiceProcess.code, logger)

    fun signUpWithProvider(@NotNull email: String, @NotNull nickname: String, @NotNull provider: String): User {
        // 이미 가입된 이메일인지 확인 필요
        if (userProviderRepository.existsByEmailAndProvider(email, provider)) {
            throw BadRequestException("alreay exists social email", ErrorCode.AlreadySocialEmail, LogEvent.UserServiceProcessError.code, logger)
        }
        if (userRepository.existsByNickname(nickname)) {
            throw BadRequestException("alreay exists nickname", ErrorCode.AlreadyNickname, LogEvent.UserServiceProcessError.code, logger)
        }

        val user = User(nickname =  nickname)
        userRepository.save(user)

        val userProvider = UserProvider(user = user, email = email, provider = provider)
        userProviderRepository.save(userProvider)

        return user
    }

    fun me(user: User): meInfoDto {
        val alreadyJoin = user.bees.size > 0

        return meInfoDto(user.nickname, alreadyJoin, user.getJoinBeeId(), user.id)
    }


    fun getUserByEmailAndProvider(email: String, provider: String): User? {
        val userProvider: UserProvider? = userProviderRepository.findByEmailAndProvider(email, provider)

        return userProvider?.user
    }

    fun isExistsNickname(nickname: String): Boolean {
        if (userRepository.existsByNickname(nickname)) {
            return false
        }

        return true
    }
}