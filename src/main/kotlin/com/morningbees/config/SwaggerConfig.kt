package com.morningbees.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.service.Parameter
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun createRestApi(): Docket {
        val tokenHeaderBuilder = ParameterBuilder()
        tokenHeaderBuilder.name("X-BEES-ACCESS-TOKEN") //헤더 이름
                .description("Access Tocken") //설명
                .modelRef(ModelRef("string"))
                .parameterType("header")
                .required(true)
                .build()

        val headers = arrayListOf<Parameter>()
        headers.add(tokenHeaderBuilder.build())

        return Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(headers)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("Morning Bees RESTFul APIs")
                .description("")
                .termsOfServiceUrl("")
                .version("1.0.0")
                .build()
    }
}