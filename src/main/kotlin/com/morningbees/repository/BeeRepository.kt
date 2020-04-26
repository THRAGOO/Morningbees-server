package com.morningbees.repository

import com.morningbees.model.Bee
import com.morningbees.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface BeeRepository : CrudRepository<Bee, Long> {
    fun getById(id: Long): Bee

    @Transactional
    fun deleteBeeById(id :Long)
}