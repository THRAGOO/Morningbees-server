package com.morningbees.dto

data class BeePenaltyDto(
    val userId: Long,
    val nickname: String,
    val penalty: Int = 0
)
