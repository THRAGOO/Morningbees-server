package com.morningbees.controller

import com.morningbees.exception.ErrorResponse
import com.morningbees.exception.InternalException
import com.morningbees.exception.MorningbeesException
import com.morningbees.util.LogEvent
import net.logstash.logback.argument.StructuredArguments
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class ErrorController {
    private val log = org.slf4j.LoggerFactory.getLogger(ErrorController::class.java)

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameter(req: HttpServletRequest, ex: java.lang.Exception): ResponseEntity<ErrorResponse> {
        log.error(ex.message, StructuredArguments.kv("eventCode", LogEvent.MissingServletRequestParameterException), StructuredArguments.kv("backTrace", ex.stackTrace[0].toString()))
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.message, 2)
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleException(req: HttpServletRequest, ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        log.warn(ex.message, StructuredArguments.kv("eventCode", LogEvent.HttpMessageNotReadableException), StructuredArguments.kv("backTrace", ex.stackTrace[0].toString()))
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "요청에 문제가 있습니다.", 3)
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MorningbeesException::class)
    fun handleMorningbeesException(req: HttpServletRequest, ex: MorningbeesException): ResponseEntity<ErrorResponse> {
        val backTrace = ex.stackTrace[0].toString() + "/" + ex.stackTrace[1].toString()
        log.warn(ex.message, StructuredArguments.kv("eventCode", LogEvent.GlobalException), StructuredArguments.kv("backTrace", backTrace))
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.code.message, ex.code.status)
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleGlobalException(req: HttpServletRequest, ex: Exception): ResponseEntity<ErrorResponse> {
        val backTrace = ex.stackTrace[0].toString() + "/" + ex.stackTrace[1].toString()
        log.warn(ex.message, StructuredArguments.kv("eventCode", LogEvent.GlobalException), StructuredArguments.kv("backTrace", backTrace))
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "요청에 문제가 있습니다.", 1)
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }


}