package com.morningbees.repository

import com.morningbees.model.Bee
import org.springframework.data.repository.CrudRepository

interface BeeRepository : CrudRepository<Bee, Long> {
}