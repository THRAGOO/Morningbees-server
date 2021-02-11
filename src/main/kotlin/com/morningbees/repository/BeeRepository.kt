package com.morningbees.repository

import com.morningbees.model.Bee
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime

interface BeeRepository : CrudRepository<Bee, Long> {
    @Transactional
    fun deleteBeeById(id :Long)
    fun findAllByEndTime(endTime: LocalTime): List<Bee>
}
