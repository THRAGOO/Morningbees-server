package com.morningbees.repository

import com.morningbees.model.Mission
import org.springframework.data.repository.CrudRepository

interface MissionRepository : CrudRepository<Mission, Long> {
}