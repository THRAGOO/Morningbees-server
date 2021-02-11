package com.morningbees.service

import com.morningbees.dto.BeeCreateDto
import com.morningbees.dto.BeeDetailInfoDto
import com.morningbees.dto.BeeInfoDto
import com.morningbees.dto.MissionInfoDto
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.Bee
import com.morningbees.model.BeeMember
import com.morningbees.model.User
import com.morningbees.repository.BeeMemberRepository
import com.morningbees.repository.BeeRepository
import com.morningbees.util.LogEvent
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period

@Service
class BeeService(
    private val beeRepository: BeeRepository,
    private val beeMemberService: BeeMemberService,
    private val beeMemberRepository: BeeMemberRepository
) {

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    fun findById(beeId: Long): Bee =
        beeRepository.findByIdOrNull(beeId)
            ?: throw BadRequestException("not find bee", ErrorCode.NotFindBee, LogEvent.BeeServiceProcess.code, logger)

    fun findByIdAndUser(beeId: Long, user: User): Bee {
        val bee = findById(beeId)
        if (!beeMemberService.isJoinUserToBee(user, bee))
            throw BadRequestException("not join user", ErrorCode.NotJoinUserToBee, LogEvent.BeeServiceProcess.code, logger)

        return bee
    }

    fun findAllByEndTime(endTime: LocalTime): List<Bee> =
        beeRepository.findAllByEndTime(endTime)

    @Transactional
    fun create(user: User, beeCreateDto: BeeCreateDto): Boolean {
        val bee: Bee = beeCreateDto.toEntity()
        beeRepository.save(bee)

        val beeMember = bee.addUser(user, BeeMember.MemberType.Manager.type)
        beeMemberService.save(beeMember)

        return true
    }

    @Transactional
    fun update(user: User, beeId: Long, beeCreateDto: BeeCreateDto): Boolean {
        val bee = findById(beeId)
        beeMemberService.checkManager(user, bee)

        bee.update(beeCreateDto.title, beeCreateDto.description, beeCreateDto.startTime, beeCreateDto.endTime, beeCreateDto.pay)

        return true
    }

    fun withdrawal(user: User): Boolean {
        val beeMember: BeeMember = beeMemberRepository.findByUser(user) ?:
        throw BadRequestException("bee member is null", ErrorCode.NotFindBee, LogEvent.BeeServiceProcess.code, logger)

        val bee: Bee = beeMember.bee

        if (beeMember.isManager() && bee.users.size > 1) {
            delegateManager(bee.users.first())
        }

        beeMemberRepository.deleteByBeeAndUser(bee, user)

        if (bee.users.size == 1) {
            beeRepository.deleteBeeById(bee.id)
        }

        return true
    }

    fun getBeeDetailInfo(
        bee: Bee,
        todayQuestionDto: MissionInfoDto?
    ): BeeDetailInfoDto {
        return BeeDetailInfoDto(
            bee.title,
            bee.startTime.hour,
            bee.endTime.hour,
            todayQuestionDto?.difficulty,
            bee.beePenalties.map { it.penalty }.sum(),
            getManager(bee).defaultInfo(),
            bee.users.size,
            getQuestioner(bee, LocalDateTime.now()).defaultInfo(),
            getQuestioner(bee, LocalDateTime.now().plusDays(1)).defaultInfo()
        )
    }

    fun getQuestioner(bee: Bee, targetDate: LocalDateTime): User {
        val users = bee.users.sortedBy { it.createdAt }
        val beesSize = users.size
        val period = Period.between(bee.createdAt.toLocalDate(), targetDate.toLocalDate()).days
        val todayQuestionerIndex = (period % beesSize)

        return users.elementAt(todayQuestionerIndex).user
    }

    fun getManager(bee: Bee): User = bee.users.last { it.type == BeeMember.MemberType.Manager.type }.user

    private fun delegateManager(delegateUser: BeeMember): Boolean {
        delegateUser.type = BeeMember.MemberType.Manager.type
        beeMemberService.save(delegateUser)

        return true
    }

    fun fetchInfos(beeId:Long, user: User): BeeInfoDto {
        val bee = findById(beeId)
        val beeMember = bee.users.last { it.user == user }

        return BeeInfoDto(
            beeMember.isManager(),
            user.nickname,
            bee.startTime,
            bee.endTime,
            bee.pay
        )
    }
}
