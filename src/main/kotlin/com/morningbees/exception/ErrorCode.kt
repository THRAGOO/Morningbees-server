package com.morningbees.exception

enum class ErrorCode(val message: String, val status: Int) {
    BadRequest("잠시 후 다시 시도해주세요.", 100),
    InvalidAccessToken("다시 로그인 해주세요.", 101),

    UnknownProvider("알 수 없는 계정 정보입니다.", 110),
    InvalidSocialToken("알 수 없는 계정 정보입니다.", 111),
    AlreadySocialEmail("이미 가입된 계정입니다.", 112),
    AlreadyNickname("이미 등록된 닉네임입니다.", 113),

}