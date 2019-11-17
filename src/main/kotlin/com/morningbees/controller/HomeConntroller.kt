package com.morningbees.controller

import com.morningbees.exception.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import net.logstash.logback.argument.StructuredArguments.kv

@RestController
class HomeController {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @GetMapping("/hello")
    fun hello(): String {
        logger.info("Hello, World!");

        return "Hello"
    }
}