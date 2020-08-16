package com.morningbees.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class HomeController {

    @GetMapping("/hello")
    fun hello(request: HttpServletRequest): String {
        return "Hello"
    }
}
