package com.morningbees.dto

import com.morningbees.model.Bee
import com.morningbees.model.Mission
import com.morningbees.model.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class MissionCreateDto(
    val beeId: Long,
    val description: String = "",
    val type: Int,
    val difficulty: Int = Mission.MissionDifficulty.Beginning.level,
    val targetDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
) {
    fun toEntity(bee: Bee, user: User, imageUrl: String, date: LocalDateTime): Mission = Mission(
        imageUrl = imageUrl,
        description = this.description,
        type = this.type,
        difficulty = this.difficulty,
        bee = bee,
        user = user,
        createdAt = date
    )
}