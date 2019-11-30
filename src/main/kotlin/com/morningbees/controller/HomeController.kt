package com.morningbees.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class HomeController {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @GetMapping("/hello")
    fun hello(request: HttpServletRequest): String {
//        (request.getAttribute("User") as User).nickname
        logger.info("Hello, World!");

        return "Hello"
    }
}