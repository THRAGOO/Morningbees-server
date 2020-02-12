package com.morningbees.controller.api.v1

import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.exception.MorningbeesException
import com.morningbees.model.Bee
import com.morningbees.model.User
import com.morningbees.service.BeeService
import com.morningbees.service.AuthService
import com.morningbees.repository.UserTokenRepository
import com.morningbees.util.LogEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Exception

@RestController
@RequestMapping("/api")
class BeeController {

    @Autowired
    lateinit var beeService: BeeService

    @Autowired
    lateinit var authService: AuthService

    @ResponseBody
    @PostMapping("/bees")
    fun createBee(@RequestHeader("morningbeesAccessToken") morningbeesAccessToken: String,
                  @RequestParam(value = "description", required = true) description: String,
                  @RequestParam(value = "title", required = true) title: String,
                  @RequestParam(value = "time", required = true) time: String,
                  @RequestParam(value = "pay", required = true) pay: Int): ResponseEntity<HashMap<String, Any>> {

        try{
            val bee: Bee = beeService.createBeeByMaster(description, title, time, pay)

            val response : HashMap<String, Any> = HashMap()
            response.put("title", title)


            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(response)
        } catch(e: MorningbeesException) {
            throw BadRequestException(e.message!!, e.code, e.logEventCode)
        } catch(e: Exception) {
            throw BadRequestException(e.message!!, ErrorCode.BadRequest, LogEvent.CreateBeeError.code)
        }
    }

    @ResponseBody
    @PostMapping("/bees/join")
    fun joinBee(@RequestHeader("morningbeesAccessToken") morningbeesAccessToken: String) {

    }

}