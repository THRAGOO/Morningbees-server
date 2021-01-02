package com.morningbees.dto

data class MainInfoDto(
        val beeInfo: BeeDetailInfoDto,
        val missions: List<MissionInfoDto> = mutableListOf()
)