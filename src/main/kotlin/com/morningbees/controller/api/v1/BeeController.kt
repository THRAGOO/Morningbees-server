
package com.morningbees.controller.api.v1

import com.morningbees.dto.BeeCreateDto
import com.morningbees.dto.BeeJoinDto
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.User
import com.morningbees.service.BeeService
import com.morningbees.service.BeeMemberService
import com.morningbees.util.LogEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
    fun create(@Valid @RequestBody beeCreateDto: BeeCreateDto, request: HttpServletRequest): ResponseEntity<Any> {
        val user: User = request.getAttribute("user") as User

        val result = beeService.create(user, beeCreateDto)
        if(!result) throw BadRequestException("fail create bee", ErrorCode.NotCreateBee, LogEvent.BeeControllerProcess.code)

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
        if(!result) throw BadRequestException("fail join bee", ErrorCode.NotJoinBee, LogEvent.BeeControllerProcess.code)

        return ResponseEntity(HttpStatus.OK)
    }

    @ResponseBody
    @DeleteMapping("/bees/withdrawal")
    fun withdrawal(request: HttpServletRequest): ResponseEntity<Any> {
        val user: User = request.getAttribute("user") as User

        val result = beeService.withdrawal(user)
        if(!result) throw BadRequestException("fail withdrawal bee", ErrorCode.FailWithdrwalBee, LogEvent.BeeControllerProcess.code)

        return ResponseEntity(HttpStatus.OK)
    }
}

