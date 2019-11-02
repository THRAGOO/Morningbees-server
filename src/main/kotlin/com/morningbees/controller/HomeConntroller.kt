package com.morningbees.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {
// pr 테스트
    @GetMapping("/hello")
    fun hello(): String = "Hello"
}