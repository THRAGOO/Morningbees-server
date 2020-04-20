
package com.morningbees.controller.api.v1

import com.morningbees.dto.BeeCreateDto
import com.morningbees.dto.BeeJoinDto
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.Bee
import com.morningbees.model.User
import com.morningbees.service.BeeService
import com.morningbees.service.BeeMemberService
import com.morningbees.util.LogEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.validation.Errors
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class BeeController {

    @Autowired
    lateinit var beeService: BeeService

    @Autowired
    lateinit var beeMemberService: BeeMemberService

    @ResponseBody
    @PostMapping("/bees")
    fun createBee(@RequestBody @Valid beeCreateDto: BeeCreateDto, request: HttpServletRequest): ResponseEntity<HashMap<String, Any>> {
        val user: User = request.getAttribute("user") as User

        val result = beeService.createBeeByManager(user, beeCreateDto)
        if(!result) throw BadRequestException("fail create bee", ErrorCode.NotCreateBee, LogEvent.BeeControllerProcess.code)

        return ResponseEntity(HttpStatus.CREATED)
    }

    @ResponseBody
    @PostMapping("/bees/join")
    fun joinBee(@RequestBody @Valid beeJoinDto: BeeJoinDto, request: HttpServletRequest): ResponseEntity<HashMap<String, Any>> {

        val result = beeMemberService.joinBeeByUser(beeJoinDto)
        if(!result) throw BadRequestException("fail join bee", ErrorCode.NotJoinUserToBee, LogEvent.BeeControllerProcess.code)

        return ResponseEntity(HttpStatus.OK)
    }


}

