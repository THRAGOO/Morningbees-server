package com.morningbees.util

enum class LogEvent(val code: String) {
    // Global Exception
    MissingServletRequestParameterException("GBE001"),
    HttpMessageNotReadableException("GBE002"),
    GlobalException("GBE003"),

    // AuthController Process
    SignUpError("SUE001"),
    SignInError("SIE001"),
    ValidNicknameError("VNE001"),
    RenewalError("RE001"),
    MeError("ME001"),

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

    // MissionService Proceess
    MissionServiceProcess("MSP001"),
}