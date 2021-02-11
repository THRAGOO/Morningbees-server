package com.morningbees.service

import com.morningbees.dto.BeePenaltyInfoDto
import com.morningbees.dto.PenaltyPaidDto
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.Bee
import com.morningbees.model.BeePenalty
import com.morningbees.model.User
import com.morningbees.repository.BeePenaltyRepository
import com.morningbees.repository.BeePenaltySupportRepository
import com.morningbees.util.LogEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BeePenaltyService(
    private val beePenaltyRepository: BeePenaltyRepository,
    private val beePenaltySupportRepository: BeePenaltySupportRepository,
    private val userService: UserService,
    private val beeMemberService: BeeMemberService
) {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    fun findAllByBeeAndUser(bee: Bee, user: User): List<BeePenalty> =
        beePenaltyRepository.findAllByBeeAndUser(bee, user)

    fun getPenaltyHistory(bee: Bee, status: Int): BeePenaltyInfoDto {
        val penaltyHistories = beePenaltySupportRepository.getTotalPenaltyHistories(bee)
        val penalties = beeMemberService.getBeeMembersWithPenalty(bee, status)

        return BeePenaltyInfoDto(penaltyHistories = penaltyHistories, penalties = penalties)
    }

    @Transactional
    fun addPendingPenalty(bee: Bee, user: User) {
        val beePenalties = findAllByBeeAndUser(bee, user)
        increasePenalty(beePenalties, bee, user, bee.pay, BeePenalty.BeePenaltyStatus.Pending)
    }

    @Transactional
    fun paid(user: User, bee: Bee, penaltyPaidDto: PenaltyPaidDto) {
        beeMemberService.checkManager(user, bee)

        penaltyPaidDto.penalties.forEach { penaltyDto ->
            val targetUser = userService.findById(penaltyDto.userId)
            val beePenalties = findAllByBeeAndUser(bee, targetUser)

            subPendingPenalty(beePenalties, penaltyDto.penalty)
            increasePenalty(beePenalties, bee, targetUser, penaltyDto.penalty, BeePenalty.BeePenaltyStatus.Pay)
        }
    }

    private fun increasePenalty(
        beePenalties: List<BeePenalty>,
        bee: Bee,
        user: User,
        penalty: Int,
        beePenaltyStatus: BeePenalty.BeePenaltyStatus
    ) {
        val paidPenalty = beePenalties.lastOrNull { it.status == beePenaltyStatus.status }

        if (paidPenalty == null) {
            save(BeePenalty(bee, user, penalty = penalty, status = beePenaltyStatus.status))
        } else {
            paidPenalty.penalty += penalty
            return
        }
    }

    private fun subPendingPenalty(
        beePenalties: List<BeePenalty>,
        penalty: Int
    ) {
        val pendingPenalty = beePenalties.last { it.status == BeePenalty.BeePenaltyStatus.Pending.status }

        if (pendingPenalty.penalty < penalty)
            throw BadRequestException("penalty bigger than current penalty", ErrorCode.BiggerPenalty, LogEvent.BeePenaltyServiceProcess.code, logger)

        pendingPenalty.penalty -= penalty
    }

    fun save(beePenalty: BeePenalty) =
        beePenaltyRepository.save(beePenalty)
}