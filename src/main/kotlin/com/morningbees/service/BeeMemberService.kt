package com.morningbees.service

import com.morningbees.dto.BeeJoinDto
import com.morningbees.dto.BeeMemberInfoDto
import com.morningbees.dto.BeePenaltyDto
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
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BeeMemberService(
    private val beeRepository: BeeRepository,
    private val beeMemberRepository: BeeMemberRepository,
    private val beeMemberRepositorySupport: BeeMemberRepositorySupport,
    private val userRepository: UserRepository
) {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    fun isJoinUserToBee(user: User, bee: Bee): Boolean = beeMemberRepository.existsByUserAndBee(user, bee)
    fun isAlreadyJoinBee(user:User) : Boolean = beeMemberRepository.existsByUser(user)

    fun checkManager(user: User, bee: Bee) {
        if(!beeMemberRepository.existsByUserAndBeeAndType(user, bee, BeeMember.MemberType.Manager.type))
            throw BadRequestException("is not manager", ErrorCode.IsNotManager, LogEvent.BeeServiceProcess.code, logger)
    }

    fun save(beeMember: BeeMember) {
        beeMemberRepository.save(beeMember)
    }

    @Transactional
    fun joinBeeByUser(beeJoinDto : BeeJoinDto): Boolean {
        val user:User = userRepository.findById(beeJoinDto.userId).orElseThrow { throw BadRequestException("not find user", ErrorCode.NotFindUser, LogEvent.BeeMemberServiceProcess.code, logger) }
        val bee:Bee = beeRepository.findById(beeJoinDto.beeId).orElseThrow { throw BadRequestException("not find bee", ErrorCode.NotFindBee, LogEvent.BeeMemberServiceProcess.code, logger) }

        if(isAlreadyJoinBee(user)) throw BadRequestException("Already join bee", ErrorCode.AlreadyJoinBee, LogEvent.BeeMemberServiceProcess.code, logger)

        val beeMember = bee.addUser(user, BeeMember.MemberType.Member.type)
        beeMemberRepository.save(beeMember)

        return true
    }

    fun getBeeMembers(beeId: Long): BeeMemberInfoDto {
        val bee = beeRepository.findById(beeId).orElseThrow { throw BadRequestException("not find bee", ErrorCode.NotFindBee, LogEvent.BeeMemberServiceProcess.code, logger) }
        val beeMembers = beeMemberRepositorySupport.getBeeMembersByBeeId(bee)
        return BeeMemberInfoDto(beeMembers)
    }

    fun getBeeMembersWithPenalty(bee: Bee, status: Int): MutableList<BeePenaltyDto> {
        return beeMemberRepositorySupport.getBeeMembersWithPenalty(bee, status)
    }
}