package com.morningbees.service

import com.morningbees.dto.BeeJoinDto
import com.morningbees.exception.BadRequestException
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

    fun isAlreadyJoinBee(user:User) : Boolean = beeMemberRepository.existsByUser(user)

    @Transactional
    fun joinBeeByUser(beeJoinDto : BeeJoinDto) : Boolean {
        val user:User = userRepository.getById(beeJoinDto.userId)
        val bee:Bee = beeRepository.getById(beeJoinDto.beeId)

        if(isAlreadyJoinBee(user)) throw BadRequestException("Already join bee")

        val beeMember = bee.addUser(user, BeeMember.MemberType.Member.type)
        beeMemberRepository.save(beeMember)

        return true
    }
}