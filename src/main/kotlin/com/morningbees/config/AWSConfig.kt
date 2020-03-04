package com.morningbees.config

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class AWSConfig : WebMvcConfigurer {
    @Value("\${aws.access-key-id}")
    private val accessKeyId: String = "key"

    @Value("\${aws.secret-key}")
    private val secretKey: String = "secret"

    @Bean
    fun AWSCredentials() : BasicAWSCredentials {
        val awsCredentials = BasicAWSCredentials(accessKeyId, secretKey)
        return awsCredentials
    }

    @Bean
    fun AwsS3Client() : AmazonS3 {
        val s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .withCredentials(EnvironmentVariableCredentialsProvider())
                .build()

        return s3Client
    }
}