package com.morningbees.controller

import com.morningbees.exception.ErrorResponse
import com.morningbees.exception.MorningbeesException
import com.morningbees.exception.MorningbeesExceptionHandler
import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
@RestController
class ErrorController {
    private val log = org.slf4j.LoggerFactory.getLogger(ErrorController::class.java)

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleBadRequest(req: HttpServletRequest, ex: java.lang.Exception): ResponseEntity<ErrorResponse> {
        log.error(ex.message)
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.message, 0)
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}