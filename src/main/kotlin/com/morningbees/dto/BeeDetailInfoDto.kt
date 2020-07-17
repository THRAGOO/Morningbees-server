package com.morningbees.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class BeeDetailInfoDto(
        @JsonProperty("title") val title: String,
        @JsonProperty("startTime") val startTime: Int,
        @JsonProperty("endTime") val endTime: Int,
        @JsonProperty("todayDifficulty") val todayDifficulty: Int,
        @JsonProperty("totalPenalty") val totalPenalty: Int,
        @JsonProperty("memberCounts") val memberCounts: Int,
        @JsonProperty("todayQuestioner") val todayQuestioner: UserInfoDto,
        @JsonProperty("nextQuestioner") val nextQuestioner: UserInfoDto) {

}
