package com.morningbees.exception

import net.logstash.logback.argument.StructuredArguments.kv
import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class MorningbeesExceptionHandler : ResponseEntityExceptionHandler() {
    private val log = org.slf4j.LoggerFactory.getLogger(MorningbeesExceptionHandler::class.java)

    @ExceptionHandler(NotFoundException::class)
    fun NotFoundException(ex :Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        val exception = (ex as MorningbeesException)
        MDC.put("logEventCode", exception.logEventCode)
        MDC.put("backTrace", exception.stackTrace[0].toString())
        log.error(exception.message)
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), exception.message, exception.code)
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InvalidParameterException::class)
    fun InvalidParameterException(ex :Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        val exception = (ex as MorningbeesException)
        MDC.put("logEventCode", exception.logEventCode)
        MDC.put("backTrace", exception.stackTrace[0].toString())
        log.error(exception.message)
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), exception.message, exception.code)
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    fun internalException(ex :Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        val exception = (ex as MorningbeesException)
        MDC.put("logEventCode", exception.logEventCode)
        MDC.put("backTrace", exception.stackTrace[0].toString())
        log.error(exception.message)
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.message, exception.code)
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}