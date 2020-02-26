
package com.morningbees.controller.api.v1

import com.morningbees.dto.CreateBeeDto
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.exception.MorningbeesException
import com.morningbees.model.Bee
import com.morningbees.model.BeeMember
import com.morningbees.model.User
import com.morningbees.service.BeeService
import com.morningbees.service.AuthService
import com.morningbees.repository.UserTokenRepository
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

    @Autowired
    lateinit var authService: AuthService

    @ResponseBody
    @PostMapping("/bees")
    fun createBee(@RequestBody @Valid CreateBeeDto:CreateBeeDto, errors:Errors): ResponseEntity<HashMap<String, Any>> {

        val title: String = CreateBeeDto.title
        val time: String = CreateBeeDto.time
        val pay: Int = CreateBeeDto.pay
        val description: String = CreateBeeDto.description

        try{
            val bee: Bee = beeService.createBeeByManager(description, title, time, pay)

            val response : HashMap<String, Any> = HashMap()
            response.put("title", title)

            return ResponseEntity.status(HttpStatus.CREATED)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(response)
        } catch(e: MorningbeesException) {
            throw BadRequestException(e.message!!, e.code, e.logEventCode)
        } catch(e: Exception) {
            throw BadRequestException(e.message!!, ErrorCode.BadRequest, LogEvent.CreateBeeError.code)
        }
    }


}

