package com.morningbees.controller.api.v1

import com.morningbees.dto.MainInfoDto
import com.morningbees.model.User
import com.morningbees.service.MainService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api")
class MainController {
    @Autowired
    lateinit var mainService: MainService

    @ResponseBody
    @GetMapping("/main")
    fun missions(@RequestParam("beeId") beeId: Long, @RequestParam("targetDate") targetDate: String, request: HttpServletRequest): ResponseEntity<MainInfoDto> {
        val user: User = request.getAttribute("user") as User
        val response = mainService.fetchMainInfo(user, beeId, targetDate)

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(response)
    }
}
