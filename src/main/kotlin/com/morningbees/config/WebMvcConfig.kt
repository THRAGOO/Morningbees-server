package com.morningbees.config

import com.morningbees.interceptor.AccessTokenValidationInterceptor
import com.morningbees.interceptor.RefreshTokenValidationInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {

    @Autowired
    lateinit var accessTokenValidationInterceptor: AccessTokenValidationInterceptor

    @Autowired
    lateinit var refreshTokenValidationInterceptor: RefreshTokenValidationInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(accessTokenValidationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/hello")
                .excludePathPatterns("/api/auth/valid_nickname")
                .excludePathPatterns("/api/auth/renewal")
                .excludePathPatterns("/api/auth/sign_in")
                .excludePathPatterns("/api/auth/sign_up")

        registry.addInterceptor(refreshTokenValidationInterceptor)
                .addPathPatterns("/api/auth/renewal")
    }
}
