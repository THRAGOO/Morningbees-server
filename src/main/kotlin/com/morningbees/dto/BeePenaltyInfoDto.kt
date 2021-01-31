package com.morningbees.dto

data class BeePenaltyInfoDto(
    val penaltyHistories: MutableList<PenaltyHistoryDto> = mutableListOf(),
    val penalties: MutableList<BeePenaltyDto> = mutableListOf()
)
