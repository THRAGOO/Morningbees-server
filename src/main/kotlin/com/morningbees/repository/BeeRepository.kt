package com.morningbees.repository

import com.morningbees.model.Bee
import com.morningbees.model.User
import org.springframework.data.repository.CrudRepository

interface BeeRepository : CrudRepository<Bee, Long> {
}