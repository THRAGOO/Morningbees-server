package com.morningbees.service

import com.morningbees.dto.BeeJoinDto
import com.morningbees.dto.BeeMemberInfoDto
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.Bee
import com.morningbees.model.BeeMember
import com.morningbees.model.User
import com.morningbees.repository.BeeMemberRepository
import com.morningbees.repository.BeeMemberRepositorySupport
import com.morningbees.repository.BeeRepository
import com.morningbees.repository.UserRepository
import com.morningbees.util.LogEvent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.Id

@Service
class BeeMemberService {

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    lateinit var beeRepository : BeeRepository
    @Autowired
    lateinit var beeMemberRepository: BeeMemberRepository
    @Autowired
    lateinit var beeMemberRepositorySupport: BeeMemberRepositorySupport
    @Autowired
    lateinit var userRepository: UserRepository

    fun isJoinUserToBee(user: User, bee: Bee): Boolean = beeMemberRepository.existsByUserAndBee(user, bee)
    fun isAlreadyJoinBee(user:User) : Boolean = beeMemberRepository.existsByUser(user)

    @Transactional
    fun joinBeeByUser(beeJoinDto : BeeJoinDto): Boolean {
        val user:User = userRepository.findById(beeJoinDto.userId).orElseThrow { throw BadRequestException("not find user", ErrorCode.NotFindUser, LogEvent.BeeMemberServiceProcess.code) }
        val bee:Bee = beeRepository.findById(beeJoinDto.beeId).orElseThrow { throw BadRequestException("not find bee", ErrorCode.NotFindBee, LogEvent.BeeMemberServiceProcess.code) }

        if(isAlreadyJoinBee(user)) throw BadRequestException("Already join bee", ErrorCode.AlreadyJoinBee, LogEvent.BeeMemberServiceProcess.code)

        val beeMember = bee.addUser(user, BeeMember.MemberType.Member.type)
        beeMemberRepository.save(beeMember)

        return true
    }

    fun getBeeMembers(beeId: Long): BeeMemberInfoDto {
        val bee = beeRepository.findById(beeId).orElseThrow { throw BadRequestException("not find bee", ErrorCode.NotFindBee, LogEvent.BeeMemberServiceProcess.code) }
        val beeMembers = beeMemberRepositorySupport.getBeeMembersByBeeId(bee)
        return BeeMemberInfoDto(beeMembers)
    }
}