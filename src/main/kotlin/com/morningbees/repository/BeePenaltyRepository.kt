package com.morningbees.repository

import com.morningbees.model.BeePenalty
import org.springframework.data.repository.CrudRepository

interface BeePenaltyRepository : CrudRepository<BeePenalty, Long> {
}