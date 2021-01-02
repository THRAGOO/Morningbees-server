
package com.morningbees.controller.api.v1

import com.morningbees.dto.BeeCreateDto
import com.morningbees.dto.BeeInfoDto
import com.morningbees.dto.BeeJoinDto
import com.morningbees.dto.BeeMemberInfoDto
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.User
import com.morningbees.service.BeeMemberService
import com.morningbees.service.BeeService
import com.morningbees.util.LogEvent
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class BeeController(
        private val beeService: BeeService,
        private val beeMemberService: BeeMemberService
) {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @ResponseBody
    @GetMapping("/bees/{id}")
    fun beeInfo(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<BeeInfoDto> {
        val user: User = request.getAttribute("user") as User

        val response = beeService.fetchInfos(id, user)

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(response)

    }

    @ResponseBody
    @GetMapping("/bees/{id}/members")
    fun members(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<BeeMemberInfoDto> {
        val response = beeMemberService.getBeeMembers(id)

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(response)
    }

    @ResponseBody
    @PostMapping("/bees")
    fun create(@Valid @RequestBody beeCreateDto: BeeCreateDto, request: HttpServletRequest): ResponseEntity<Any> {
        val user: User = request.getAttribute("user") as User

        val result = beeService.create(user, beeCreateDto)
        if(!result) throw BadRequestException("fail create bee", ErrorCode.NotCreateBee, LogEvent.BeeControllerProcess.code, logger)

        return ResponseEntity(HttpStatus.CREATED)
    }

    @ResponseBody
    @PutMapping("/bees/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody beeCreateDto: BeeCreateDto, request: HttpServletRequest): ResponseEntity<Any> {
        val user: User = request.getAttribute("user") as User

        beeService.update(user, id, beeCreateDto)

        return ResponseEntity(HttpStatus.OK)
    }

    @ResponseBody
    @PostMapping("/bees/join")
    fun joinBee(@RequestBody beeJoinDto: BeeJoinDto, request: HttpServletRequest): ResponseEntity<Any> {

        val result = beeMemberService.joinBeeByUser(beeJoinDto)
        if(!result) throw BadRequestException("fail join bee", ErrorCode.NotJoinBee, LogEvent.BeeControllerProcess.code, logger)

        return ResponseEntity(HttpStatus.OK)
    }

    @ResponseBody
    @DeleteMapping("/bees/withdrawal")
    fun withdrawal(request: HttpServletRequest): ResponseEntity<Any> {
        val user: User = request.getAttribute("user") as User

        val result = beeService.withdrawal(user)
        if(!result) throw BadRequestException("fail withdrawal bee", ErrorCode.FailWithdrwalBee, LogEvent.BeeControllerProcess.code, logger)

        return ResponseEntity(HttpStatus.OK)
    }
}

