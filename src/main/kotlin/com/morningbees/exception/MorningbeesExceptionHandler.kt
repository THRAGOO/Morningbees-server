package com.morningbees.exception

import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class MorningbeesExceptionHandler : ResponseEntityExceptionHandler() {
    private val log = org.slf4j.LoggerFactory.getLogger(MorningbeesExceptionHandler::class.java)

    @ExceptionHandler(NotFoundException::class)
    fun NotFoundException(ex :NotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val exception = (ex as MorningbeesException)
        MDC.put("logEventCode", exception.logEventCode)
        MDC.put("backTrace", exception.stackTrace[0].toString())
        log.error(exception.message)
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), exception.message, exception.code)
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InvalidParameterException::class)
    fun InvalidParameterException(ex :InvalidParameterException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val exception = (ex as MorningbeesException)
        MDC.put("logEventCode", exception.logEventCode)
        MDC.put("backTrace", exception.stackTrace[0].toString())
        log.error(exception.message)
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), exception.message, exception.code)
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InternalException::class)
    fun InternalException(ex :InternalException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val exception = (ex as MorningbeesException)
        MDC.put("logEventCode", exception.logEventCode)
        MDC.put("backTrace", exception.stackTrace[0].toString())
        log.error(exception.message)
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.message, exception.code)
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(BadRequestException::class)
    fun BadRequestException(ex :BadRequestException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val exception = (ex as MorningbeesException)
        MDC.put("logEventCode", exception.logEventCode)
        MDC.put("backTrace", exception.stackTrace[0].toString())
        log.error(exception.message)
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), exception.message, exception.code)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UnAuthorizeException::class)
    fun UnAuthorizeException(ex :UnAuthorizeException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val exception = (ex as MorningbeesException)
        MDC.put("logEventCode", exception.logEventCode)
        MDC.put("backTrace", exception.stackTrace[0].toString())
        log.error(exception.message)
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), exception.message, exception.code)
        return ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED)
    }
}
