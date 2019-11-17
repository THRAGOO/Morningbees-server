package com.morningbees.exception

import java.time.LocalDateTime
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty

data class ErrorResponse(
        @JsonProperty("timestamp")
        @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
        val timestamp: LocalDateTime? = null,
        val status: Int = 0,
        val message: String?,
        val code: Int = 0
)
