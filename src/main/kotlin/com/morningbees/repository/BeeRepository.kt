package com.morningbees.repository

import com.morningbees.dto.BeeInfoDto
import com.morningbees.model.*
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.annotation.Resource
import com.querydsl.jpa.impl.JPAQueryFactory
import com.querydsl.core.types.ConstantImpl
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPQLQuery
import com.querydsl.core.types.dsl.CaseBuilder
import org.hibernate.criterion.Projections
import org.springframework.transaction.annotation.Transactional

interface BeeRepository : CrudRepository<Bee, Long> {
    fun getById(id: Long): Bee

    @Transactional
    fun deleteBeeById(id :Long)
}

@Repository
class BeeRepositorySupport(

        @Resource(name = "jpaQueryFactory")
        val query: JPAQueryFactory

) : QuerydslRepositorySupport(Bee::class.java) {

    fun fetchBeeInfosByBeeAndCreatedAt(bee: Bee, user:User): List<BeeInfoDto> {
        val qUser = QUser.user
        val qBee = QBee.bee
        val qBeeMember = QBeeMember.beeMember
        val beeInfos: JPQLQuery<BeeInfoDto> = query.from(qBee)
                .innerJoin(qUser.bees, qBeeMember)
                .select(Projections.constructor(BeeInfoDto::class.java,
                        qBeeMember.type, qUser.nickname, qBee.users, qBee.startTime, qBee.endTime, qBee.beePenalties,
                ))


        return beeInfos.fetch()
    }
}