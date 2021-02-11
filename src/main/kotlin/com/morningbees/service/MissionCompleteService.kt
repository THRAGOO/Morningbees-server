package com.morningbees.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class MissionCompleteService(
    private val beeService: BeeService,
    private val beePenaltyService: BeePenaltyService
) {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Transactional
    fun complete() {
        logger.info("start")
        val bees = beeService.findAllByEndTime(generateCurrentOnTime())

        bees.forEach { bee ->
            bee.users.forEach { beeMember ->
                val user = beeMember.user
                val mission = user.missions.lastOrNull { it.createdAt.isAfter(generateCurrentStartDay()) }

                if (mission == null)
                    beePenaltyService.addPendingPenalty(bee, user)
            }
        }
        logger.info("end")
    }

    private fun generateCurrentOnTime(): LocalTime {
        return LocalTime.of(LocalTime.now().hour, 0, 0)
    }

    private fun generateCurrentStartDay(): LocalDateTime {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
    }
}
