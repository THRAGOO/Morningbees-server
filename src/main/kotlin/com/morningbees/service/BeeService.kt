package com.morningbees.service

import com.morningbees.dto.BeeCreateDto
import com.morningbees.model.Bee
import com.morningbees.model.BeeMember
import com.morningbees.model.User
import com.morningbees.repository.BeeMemberRepository
import com.morningbees.repository.BeeRepository
import com.morningbees.util.LogEvent
import net.logstash.logback.argument.StructuredArguments.kv
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BeeService {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    lateinit var beeRepository : BeeRepository

    @Autowired
    lateinit var beeMemberRepository: BeeMemberRepository

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

}