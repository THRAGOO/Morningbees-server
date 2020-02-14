package com.morningbees.controller

import com.morningbees.exception.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class ErrorController {
    private val log = org.slf4j.LoggerFactory.getLogger(ErrorController::class.java)

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameter(req: HttpServletRequest, ex: java.lang.Exception): ResponseEntity<ErrorResponse> {
        log.error(ex.message)
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.message, 0)
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleException(req: HttpServletRequest, ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        log.warn(ex.message)
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "요청에 문제가 있습니다.", 0)
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}