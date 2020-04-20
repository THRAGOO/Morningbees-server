package com.morningbees.service

import com.morningbees.dto.BeeJoinDto
import com.morningbees.model.Bee
import com.morningbees.model.BeeMember
import com.morningbees.model.User
import com.morningbees.repository.BeeMemberRepository
import com.morningbees.repository.BeeRepository
import com.morningbees.repository.UserRepository
import com.morningbees.util.LogEvent
import net.logstash.logback.argument.StructuredArguments.kv
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BeeMemberService {

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    lateinit var beeRepository : BeeRepository

    @Autowired
    lateinit var beeMemberRepository: BeeMemberRepository

    @Autowired
    lateinit var userRepository: UserRepository

    fun isJoinUserToBee(user: User, bee: Bee): Boolean = beeMemberRepository.existsByUserAndBee(user, bee)

    @Transactional
    fun joinBeeByUser(beeJoinDto : BeeJoinDto) : Boolean {
        try {
            val user:User = userRepository.getById(beeJoinDto.userId)
            val bee:Bee = beeRepository.getById(beeJoinDto.beeId)

            if(beeMemberRepository.existsByUserAndBee(user, bee)== true) throw Exception("Already join bee")

            val beeMember = bee.addUser(user, BeeMember.MemberType.Member.type)
            beeMemberRepository.save(beeMember)

            return true
        } catch(ex: Exception) {
            logger.warn(ex.message, kv("userId", beeJoinDto.userId), kv("eventCode", LogEvent.BeeMemberServiceProcess.code))
            return false
        }

    }
}