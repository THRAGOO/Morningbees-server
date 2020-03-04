package com.morningbees.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
class HomeController {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @GetMapping("/hello")
    fun hello(request: HttpServletRequest): String {
        return "Hello"
    }
}
