package com.morningbees.repository

import com.morningbees.dto.PenaltyHistoryDto
import com.morningbees.model.Bee
import com.morningbees.model.BeePenalty
import com.morningbees.model.QBeePenalty.beePenalty
import com.morningbees.model.User
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

interface BeePenaltyRepository : JpaRepository<BeePenalty, Long> {

    fun findAllByBeeAndUser(bee: Bee, user: User): List<BeePenalty>
}

@Repository
class BeePenaltySupportRepository(
    private val jpaQueryFactory: JPAQueryFactory
): QuerydslRepositorySupport(BeePenalty::class.java) {

    fun getTotalPenaltyHistories(bee: Bee): MutableList<PenaltyHistoryDto> {

        return jpaQueryFactory
            .select(
                Projections.constructor(
                    PenaltyHistoryDto::class.java,
                    beePenalty.status,
                    beePenalty.penalty.sum()
                )
            )
            .from(beePenalty)
            .where(beePenalty.bee.eq(bee))
            .groupBy(beePenalty.bee, beePenalty.status)
            .fetch()
    }
}
