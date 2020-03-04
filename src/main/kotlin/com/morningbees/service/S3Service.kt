package com.morningbees.service

import com.amazonaws.services.s3.AmazonS3
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.util.LogEvent
import net.logstash.logback.argument.StructuredArguments
import org.springframework.beans.factory.annotation.Value
import java.io.IOException
import org.springframework.web.multipart.MultipartFile
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

@Service
class S3Service {
    @Autowired
    lateinit var s3Client: AmazonS3

    @Value("\${aws.bucket}")
    private val bucket: String = "key"

    @Throws(IOException::class)
    fun upload(file: MultipartFile): String {
        try {
            var fileFormat: String? = file.contentType?.split("/")?.last()
            if (fileFormat == null) fileFormat = "jpg"

            val fileName = fileNameToHashString(file.originalFilename.toString()).substring(0, 10) + "." + fileFormat

            val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyyMMdd")
            val bucketName: String = bucket + "/" + dateFormat.format(Date())
            s3Client.putObject(PutObjectRequest(bucketName, fileName, file.inputStream, null).withCannedAcl(CannedAccessControlList.PublicRead))
            return s3Client.getUrl(bucketName, fileName).toString()
        } catch (ex: Exception) {
            throw BadRequestException(ex.message!!, ErrorCode.BadRequest, LogEvent.S3ServiceProcessError.code)
        }
    }

    private fun fileNameToHashString(input: String): String {
        return MessageDigest
                .getInstance("SHA-256")
                .digest(input.toByteArray())
                .fold("", { str, it -> str + "%02x".format(it) })
    }
}