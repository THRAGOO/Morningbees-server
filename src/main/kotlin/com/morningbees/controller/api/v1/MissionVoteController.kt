package com.morningbees.controller.api.v1

import com.morningbees.dto.MissionVoteDto
import com.morningbees.model.User
import com.morningbees.service.MissionVoteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/api")
class MissionVoteController {

    @Autowired
    lateinit var missionVoteService: MissionVoteService

    @ResponseBody
    @PostMapping("/vote")
    fun vote(@RequestBody missionVoteDto: MissionVoteDto, request: HttpServletRequest): ResponseEntity<Any> {
        val user: User = request.getAttribute("user") as User

        val result = missionVoteService.action(user, missionVoteDto)
        return ResponseEntity(HttpStatus.OK)
    }
}