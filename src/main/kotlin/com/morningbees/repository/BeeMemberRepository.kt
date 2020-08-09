package com.morningbees.repository

import com.morningbees.model.Bee
import com.morningbees.model.BeeMember
import com.morningbees.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface BeeMemberRepository : CrudRepository<BeeMember, Long> {

    @Transactional
    fun deleteByBeeAndUser(bee: Bee, user: User)

    fun findByUser(user: User): BeeMember?
    fun existsByUserAndBeeAndType(user: User, bee: Bee, type: Int): Boolean
    fun existsByUserAndBee(user: User, bee: Bee): Boolean
    fun existsByUser(user:User): Boolean
}