package com.morningbees.controller.api.v1

import com.morningbees.dto.BeePenaltyInfoDto
import com.morningbees.dto.PenaltyPaidDto
import com.morningbees.model.User
import com.morningbees.service.BeePenaltyService
import com.morningbees.service.BeeService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
class BeePenaltyController(
    val beeService: BeeService,
    val beePenaltyService: BeePenaltyService
) {

    @GetMapping("/api/bee_penalties/{beeId}")
    fun beePenalties(@PathVariable beeId: Long, status: Int, request: HttpServletRequest): ResponseEntity<BeePenaltyInfoDto> {
        val user: User = request.getAttribute("user") as User
        val bee = beeService.findByIdAndUser(beeId, user)

        val response = beePenaltyService.getPenaltyHistory(bee, status)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(response)
    }

    @PostMapping("/api/bee_penalties/paid/{beeId}")
    fun paid(
        @PathVariable beeId: Long,
        @Valid @RequestBody penaltyPaidDto: PenaltyPaidDto,
        request: HttpServletRequest
    ): ResponseEntity<Any> {
        val user: User = request.getAttribute("user") as User
        val bee = beeService.findByIdAndUser(beeId, user)

        beePenaltyService.paid(user, bee, penaltyPaidDto)

        return ResponseEntity(HttpStatus.OK)
    }

}