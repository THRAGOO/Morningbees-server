package com.morningbees.repository

import com.morningbees.dto.BeePenaltyDto
import com.morningbees.dto.UserInfoDto
import com.morningbees.model.*
import com.morningbees.model.QBeeMember.beeMember
import com.morningbees.model.QBeePenalty.beePenalty
import com.morningbees.model.QUser.user
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions.asString
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.annotation.Resource

interface BeeMemberRepository : CrudRepository<BeeMember, Long> {

    @Transactional
    fun deleteByBeeAndUser(bee: Bee, user: User)

    fun findByUser(user: User): BeeMember?
    fun existsByUserAndBeeAndType(user: User, bee: Bee, type: Int): Boolean
    fun existsByUserAndBee(user: User, bee: Bee): Boolean
    fun existsByUser(user:User): Boolean
}

@Repository
class BeeMemberRepositorySupport(

    @Resource(name = "jpaQueryFactory")
    val query: JPAQueryFactory

) : QuerydslRepositorySupport(BeeMember::class.java) {

    fun getBeeMembersByBeeId(bee: Bee): List<UserInfoDto> {
        val qBeeMember = QBeeMember.beeMember
        val qUser = QUser.user

        return query.selectFrom(qBeeMember)
            .select(Projections.constructor(
                UserInfoDto::class.java,
                qUser.id,
                qUser.nickname,
                asString("https://thragoo-test.s3.ap-northeast-2.amazonaws.com/temp_profile_image.png").`as`("profileImage")))
            .innerJoin(qBeeMember.user, qUser)
            .where(qBeeMember.bee.eq(bee))
            .fetch()
    }

    fun getBeeMembersWithPenalty(bee: Bee, status: Int): MutableList<BeePenaltyDto> {
        return query
            .select(
                Projections.constructor(
                    BeePenaltyDto::class.java,
                    user.id,
                    user.nickname,
                    beePenalty.penalty
                )
            )
            .from(beeMember)
            .innerJoin(beeMember.user, user)
            .leftJoin(user.beePenalties, beePenalty)
            .on(beePenalty.bee.eq(bee))
            .where(beeMember.bee.eq(bee))
            .where(beePenalty.status.eq(status))
            .fetch()
    }
}