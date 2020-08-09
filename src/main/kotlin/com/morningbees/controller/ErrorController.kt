package com.morningbees.controller

import com.morningbees.exception.ErrorCode
import com.morningbees.exception.ErrorResponse
import com.morningbees.exception.MorningbeesException
import com.morningbees.util.LogEvent
import net.logstash.logback.argument.StructuredArguments
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class ErrorController {
    private val log = org.slf4j.LoggerFactory.getLogger(ErrorController::class.java)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(req: HttpServletRequest, ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        log.warn(ex.message, StructuredArguments.kv("eventCode", LogEvent.MissingServletRequestParameterException))
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ErrorCode.InvalidParameter.message, ErrorCode.InvalidParameter.status)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameter(req: HttpServletRequest, ex: MissingServletRequestParameterException): ResponseEntity<ErrorResponse> {
        log.warn(ex.message, StructuredArguments.kv("eventCode", LogEvent.MissingServletRequestParameterException))
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ErrorCode.BadRequest.message, ErrorCode.BadRequest.status)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(req: HttpServletRequest, ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        log.warn(ex.message, StructuredArguments.kv("eventCode", LogEvent.HttpMessageNotReadableException))
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ErrorCode.BadRequest.message, ErrorCode.BadRequest.status)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MorningbeesException::class)
    fun handleMorningbeesException(req: HttpServletRequest, ex: MorningbeesException): ResponseEntity<ErrorResponse> {
        log.warn(ex.message, StructuredArguments.kv("eventCode", LogEvent.GlobalException))
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.code.message, ex.code.status)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception::class)
    fun handleGlobalException(req: HttpServletRequest, ex: Exception): ResponseEntity<ErrorResponse> {
        log.warn(ex.message, StructuredArguments.kv("eventCode", LogEvent.GlobalException))
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ErrorCode.BadRequest.message, ErrorCode.BadRequest.status)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }


}