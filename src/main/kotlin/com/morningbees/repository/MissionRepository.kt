package com.morningbees.repository

import com.morningbees.model.Bee
import com.morningbees.model.Mission
import com.morningbees.model.QMission
import com.morningbees.model.User
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.annotation.Resource
import com.querydsl.core.types.ConstantImpl
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.StringTemplate




interface MissionRepository : CrudRepository<Mission, Long> {
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

}