package com.morningbees.util

enum class LogEvent(val code: String) {
    // AuthController Process
    SignUpError("SUE001"),
    SignInError("SIE001"),
    ValidNicknameError("VNE001"),

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
}