package com.morningbees.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class BeeMemberInfoDto(
        @JsonProperty("members") val members: List<UserInfoDto>
)
