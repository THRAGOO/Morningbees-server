package com.morningbees.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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
import org.springframework.web.util.ContentCachingRequestWrapper
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class ErrorController {
    private val logger = org.slf4j.LoggerFactory.getLogger(this.javaClass.name)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(req: HttpServletRequest, ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        logger.warn(ex.message, StructuredArguments.kv("eventCode", LogEvent.MissingServletRequestParameterException))
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ErrorCode.InvalidParameter.message, ErrorCode.InvalidParameter.status)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameter(req: HttpServletRequest, ex: MissingServletRequestParameterException): ResponseEntity<ErrorResponse> {
        logger.warn(ex.message, StructuredArguments.kv("eventCode", LogEvent.MissingServletRequestParameterException))
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ErrorCode.BadRequest.message, ErrorCode.BadRequest.status)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(req: HttpServletRequest, ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        logger.warn(ex.message, StructuredArguments.kv("eventCode", LogEvent.HttpMessageNotReadableException))
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ErrorCode.BadRequest.message, ErrorCode.BadRequest.status)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MorningbeesException::class)
    fun handleMorningbeesException(req: HttpServletRequest, ex: MorningbeesException): ResponseEntity<ErrorResponse> {
        ex.logger.warn(ex.message, StructuredArguments.kv("eventCode", ex.logEventCode))
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.code.message, ex.code.status)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception::class)
    fun handleGlobalException(req: HttpServletRequest, ex: Exception): ResponseEntity<ErrorResponse> {
        logger.warn(ex.message, StructuredArguments.kv("eventCode", LogEvent.GlobalException), StructuredArguments.kv("body", buildRequestBody(req)))
        val errorResponse = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ErrorCode.BadRequest.message, ErrorCode.BadRequest.status)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    fun buildRequestBody(request: HttpServletRequest): HashMap<String, Any> {
        val data = (request as ContentCachingRequestWrapper).contentAsByteArray

        var parameters = hashMapOf<String, Any>()
        if (data.isNotEmpty()) parameters = ObjectMapper().readValue(data)

        request.parameterMap.forEach { (k, v) ->
            parameters[k] = v.first()
        }

        return parameters
    }

}