package com.morningbees.repository

import com.morningbees.dto.MissionInfoDto
import com.morningbees.dto.UserInfoDto
import com.morningbees.model.*
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions.asString
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.annotation.Resource
import javax.persistence.Id

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
                        qUser.nickname,
                        asString("https://thragoo-test.s3.ap-northeast-2.amazonaws.com/temp_profile_image.png").`as`("profileImage")))
                .innerJoin(qBeeMember.user, qUser)
                .where(qBeeMember.bee.eq(bee))
                .fetch()
    }
}