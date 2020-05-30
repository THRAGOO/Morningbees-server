package com.morningbees.controller.api.v1

import com.morningbees.dto.MissionCreateDto
import com.morningbees.dto.MissionInfoDto
import com.morningbees.model.User
import com.morningbees.service.MissionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/api")
class MissionController {
    @Autowired
    lateinit var missionService: MissionService

    @ResponseBody
    @PostMapping("/missions", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun create(@RequestParam("image") image: MultipartFile, @ModelAttribute missionCreateDto: MissionCreateDto, request: HttpServletRequest): ResponseEntity<Any> {
        val user: User = request.getAttribute("user") as User

        val currnetDate = SimpleDateFormat("yyyyMMdd").format(Date())

        missionService.create(user, image, missionCreateDto, currnetDate)

        return ResponseEntity(HttpStatus.CREATED)
    }

    @ResponseBody
    @GetMapping("/missions")
    fun missions(@RequestParam("beeId") beeId: Long, @RequestParam("targetDate") targetDate: String, request: HttpServletRequest): ResponseEntity<List<MissionInfoDto>> {
        val response = missionService.fetchInfos(beeId, targetDate)

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(response)
    }
}