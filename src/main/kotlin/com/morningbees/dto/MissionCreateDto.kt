package com.morningbees.dto

import com.morningbees.model.Bee
import com.morningbees.model.Mission
import com.morningbees.model.User

data class MissionCreateDto(val beeId: Long, val description: String, val type: Int, val difficulty: Int) {
    fun toEntity(bee: Bee, user: User, imageUrl: String): Mission = Mission(imageUrl, this.description, this.difficulty, this.type, bee, user)
}