package com.morningbees.repository

import com.morningbees.dto.MissionInfoDto
import com.morningbees.model.*
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.annotation.Resource
import com.querydsl.core.types.ConstantImpl
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPQLQuery
import com.querydsl.core.types.dsl.CaseBuilder
import java.math.BigDecimal
import java.time.format.DateTimeFormatter


interface MissionRepository : CrudRepository<Mission, Long> {
    fun getById(id: Long): Mission?
}

@Repository
class MissionRepositorySupport(

        @Resource(name = "jpaQueryFactory")
        val query: JPAQueryFactory

) : QuerydslRepositorySupport(Mission::class.java) {

    fun findByUserAndBeeAndCreatedAt(user: User, bee: Bee, date: String): Mission? {
        val qMission = QMission.mission
        val dateFormat = Expressions.stringTemplate("DATE_FORMAT({0}, {1})", qMission.createdAt, ConstantImpl.create<String>("%Y-%m-%d"))

        return query.selectFrom(qMission)
                .where(qMission.user.eq(user).and(dateFormat.eq(date)).and(qMission.bee.eq(bee)))
                .fetchFirst()
    }

    fun fetchMissionInfosByBeeAndCreatedAt(bee: Bee, date: String): List<MissionInfoDto> {
        val qUser = QUser.user
        val qMissionVote = QMissionVote.missionVote
        val qMission = QMission.mission
        val dateFormat = Expressions.stringTemplate("DATE_FORMAT({0}, {1})", qMission.createdAt, ConstantImpl.create<String>("%Y-%m-%d"))

        val missionInfos: JPQLQuery<MissionInfoDto> = query.from(qMission)
                .innerJoin(qMission.user, qUser)
                .leftJoin(qMission.missionVotes, qMissionVote)
                .select(Projections.constructor(MissionInfoDto::class.java,
                        qMission.id, qMission.imageUrl, qUser.nickname, qMission.type, qMission.difficulty,
                        Expressions.stringTemplate("DATE_FORMAT({0}, {1})", qMission.createdAt, ConstantImpl.create<String>("%Y-%m-%d %H:%i:%S")),
                        CaseBuilder()
                        .`when`(qMissionVote.type.eq(MissionVote.VoteType.Agree.type))
                        .then(BigDecimal.ONE)
                        .otherwise(BigDecimal.ZERO).sum().`as`("agreeCount"),
                        CaseBuilder()
                        .`when`(qMissionVote.type.eq(MissionVote.VoteType.Disagree.type))
                        .then(BigDecimal.ONE)
                        .otherwise(BigDecimal.ZERO).sum().`as`("disagreeCount")
                ))
                .groupBy(qMission.id, qMission.imageUrl, qMission.type, qMission.difficulty, qUser.nickname).orderBy(qMission.type.asc())
                .where(dateFormat.eq(date).and(qMission.bee.eq(bee)))

        return missionInfos.fetch()
    }
}