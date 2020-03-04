package com.morningbees.controller.api.v1

import com.morningbees.dto.MissionCreateDto
import com.morningbees.model.User
import com.morningbees.service.MissionService
import com.morningbees.util.Token
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/api")
class MissionController {
    @Autowired
    lateinit var missionService: MissionService
    @Autowired
    lateinit var token: Token

    @ResponseBody
    @PostMapping("/missions", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun create(@RequestParam("file") file: MultipartFile, @ModelAttribute missionCreateDto: MissionCreateDto, request: HttpServletRequest): ResponseEntity<Any> {
        val user: User = request.getAttribute("user") as User
        missionService.create(user, file, missionCreateDto)

        return ResponseEntity(HttpStatus.CREATED)
    }
}