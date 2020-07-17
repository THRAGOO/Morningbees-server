package com.morningbees.service

import com.morningbees.dto.BeeCreateDto
import com.morningbees.dto.BeeDetailInfoDto
import com.morningbees.dto.MissionInfoDto
import com.morningbees.model.Bee
import com.morningbees.model.BeeMember
import com.morningbees.model.Mission
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
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

@Service
class BeeService {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    lateinit var beeRepository : BeeRepository

    @Autowired
    lateinit var beeMemberRepository: BeeMemberRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Transactional
    fun createBeeByManager(user: User, beeCreateDto: BeeCreateDto): Boolean {
        try {
            if (beeCreateDto.startTime < 6) throw Exception("not match startTime")
            if (beeCreateDto.endTime > 10) throw Exception("not match endTime")
            if (beeCreateDto.pay < 2000 || beeCreateDto.pay > 10000) throw Exception("not match pay")
            val bee: Bee = beeCreateDto.toEntity()
            beeRepository.save(bee)

            val beeMember = bee.addUser(user, BeeMember.MemberType.Manager.type)
            beeMemberRepository.save(beeMember)

            return true
        } catch (ex: Exception) {
            logger.warn(ex.message, kv("userId", user.id), kv("eventCode", LogEvent.BeeServiceProcess.code))
            return false
        }
    }

    fun withdrawal(user: User): Boolean {
        try {
            val beeMember: BeeMember? = beeMemberRepository.findByUser(user)
            if (beeMember == null) throw Exception("bee member is null")

            val bee: Bee? = beeMember.bee
            if (bee == null) throw Exception("bee is null")

            if (beeMember.isManager() && bee.users.size > 1) {
                delegateManager(bee.users.first())
            }

            beeMemberRepository.deleteByBeeAndUser(bee, user)

            if (bee.users.size == 1) {
                beeRepository.deleteBeeById(bee.id!!)
            }

            return true
        } catch (ex: Exception) {
            logger.warn(ex.message, kv("userId", user.id), kv("eventCode", LogEvent.BeeServiceProcess.code), kv("backTrace", ex.stackTrace[0].toString() + ex.stackTrace[1].toString()))
            return false
        }
    }

    fun getBeeDetailInfo(bee: Bee, todayQuestionDto: MissionInfoDto): BeeDetailInfoDto = BeeDetailInfoDto(bee.title,
            bee.startTime.hour,
            bee.endTime.hour,
            todayQuestionDto.difficulty,
            bee.beePenalties.map { it.penalty }.sum(),
            bee.users.size,
            getQuestioner(bee, LocalDateTime.now()).defaultInfo(),
            getQuestioner(bee, LocalDateTime.now().plusDays(1)).defaultInfo())

    fun getQuestioner(bee: Bee, targetDate: LocalDateTime): User {
        val users = bee.users.sortedBy { it.createdAt }
        val beesSize = users.size
        val period = Duration.between(bee.createdAt, targetDate).toDays()
        val todayQuestionerIndex = (period % beesSize).toInt()

        return users.elementAt(todayQuestionerIndex).user
    }

    private
    fun delegateManager(delegateUser: BeeMember): Boolean {
        val anotherBeeMember = delegateUser
        anotherBeeMember.type = BeeMember.MemberType.Manager.type
        beeMemberRepository.save(anotherBeeMember)

        return true
    }
}