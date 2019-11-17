package com.morningbees

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@SpringBootApplication
@ServletComponentScan
class MorningbeesApplication

fun main(args: Array<String>) {
    runApplication<MorningbeesApplication>(*args){
        setBannerMode(Banner.Mode.OFF)
    }
}
