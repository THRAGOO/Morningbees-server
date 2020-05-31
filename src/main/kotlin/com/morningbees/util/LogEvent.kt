package com.morningbees.util

enum class LogEvent(val code: String) {
    // Global Exception
    MissingServletRequestParameterException("GBE001"),
    HttpMessageNotReadableException("GBE002"),
    GlobalException("GBE003"),

    // Interceptor Process
    AccessTokenInterceptorProcess("AIP001"),
    RefreshTokenInterceptorProcess("RIP001"),

    // UserService Process
    UserServiceProcessError("USPE001"),

    // SocialLoginService Process
    SocialLoginServiceProcessError("SLSE001"),

    // SocialLoginFactory Process
    SocialLoginFactoryProcessError("SLF001"),

    // TokenService Process
    TokenServiceProcessError("TSE001"),

    // AuthService Process
    AuthServiceProcessError("ASE001"),

    //BeeController Process
    CreateBeeError("CBE001"),

    // S3Service Process
    S3ServiceProcessError("SSE001"),

    // MissionService Proccess
    MissionServiceProcess("MSP001"),

    // BeeService Process
    BeeServiceProcess("BSP001"),

    //BeeMemberService Process
    BeeMemberServiceProcess("BMSP001"),

    // BeeController Process
    BeeControllerProcess("BCP001"),

    // MissionVoteService Process
    MissionVoteServiceProcess("MVP001")
}