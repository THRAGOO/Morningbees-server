
package com.morningbees.controller.api.v1

import com.morningbees.dto.CreateBeeDto
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.exception.MorningbeesException
import com.morningbees.model.Bee
import com.morningbees.model.BeeMember
import com.morningbees.model.User
import com.morningbees.service.BeeService
import com.morningbees.util.LogEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import org.slf4j.LoggerFactory
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.Errors
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class BeeController {

    @Autowired
    lateinit var beeService: BeeService

    @ResponseBody
    @PostMapping("/bees")
    fun createBee(@RequestBody @Valid CreateBeeDto:CreateBeeDto, request : HttpServletRequest, errors:Errors): ResponseEntity<HashMap<String, Any>> {

        val user: User = request.getAttribute("user") as User

        val bee: Bee = beeService.createBeeByManager(user, CreateBeeDto.description, CreateBeeDto.title, CreateBeeDto.time, CreateBeeDto.pay)

        return ResponseEntity(HttpStatus.CREATED)
    }


}

