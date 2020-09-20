package com.morningbees.controller

import com.morningbees.service.S3Service
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest

@RestController
class HomeController(
        val s3Service: S3Service
) {

    @GetMapping("/hello")
    fun hello(request: HttpServletRequest): String {
        return "Hello"
    }

    @ResponseBody
    @PostMapping("/image/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(@RequestParam("image") image: MultipartFile): ResponseEntity<HashMap<String, String>> {
        val imageUrl: String = s3Service.upload(image)

        return ResponseEntity(hashMapOf("image_url" to imageUrl), HttpStatus.CREATED)
    }
}
