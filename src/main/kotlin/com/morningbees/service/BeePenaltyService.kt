package com.morningbees.service

import com.morningbees.dto.BeePenaltyInfoDto
import com.morningbees.dto.PenaltyUnpaidDto
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

    fun getPenaltyHistory(bee: Bee, status: Int): BeePenaltyInfoDto {
        val penaltyHistories = beePenaltySupportRepository.getTotalPenaltyHistories(bee)
        val penalties = beeMemberService.getBeeMembersWithPenalty(bee, status)

        return BeePenaltyInfoDto(penaltyHistories = penaltyHistories, penalties = penalties)
    }

    @Transactional
    fun paid(user: User, bee: Bee, penaltyUnpaidDto: PenaltyUnpaidDto) {
        beeMemberService.checkManager(user, bee)

        penaltyUnpaidDto.userIds.forEach { userId ->
            val targetUser = userService.findById(userId)
            val beePenalties = beePenaltyRepository.findAllByBeeAndUser(bee, targetUser)

            subPendingPenalty(beePenalties, penaltyUnpaidDto.penalty)
            addPaidPenalty(beePenalties, bee, targetUser, penaltyUnpaidDto.penalty)
        }
    }

    private fun addPaidPenalty(
        beePenalties: List<BeePenalty>,
        bee: Bee,
        targetUser: User,
        penalty: Int
    ) {
        val paidPenalty = beePenalties.lastOrNull { it.status == BeePenalty.BeePenaltyStatus.Pay.status }

        if (paidPenalty == null) {
            save(BeePenalty(bee, targetUser, penalty = penalty, status = BeePenalty.BeePenaltyStatus.Pay.status))
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