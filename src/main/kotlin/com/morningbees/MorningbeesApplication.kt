package com.morningbees

import com.morningbees.config.TokenConfig
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@SpringBootApplication
@ServletComponentScan
@EnableConfigurationProperties(TokenConfig::class)
class MorningbeesApplication

fun main(args: Array<String>) {
    runApplication<MorningbeesApplication>(*args){
        setBannerMode(Banner.Mode.OFF)
    }
}
