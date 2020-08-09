package com.morningbees.exception

enum class ErrorCode(val message: String, val status: Int) {
    BadRequest("잠시 후 다시 시도해주세요.", 100),
    InvalidAccessToken("다시 로그인 해주세요.", 101),
    InvalidParameter("요청에 문제가 있습니다.", 102),

    UnknownProvider("알 수 없는 계정 정보입니다.", 110),
    InvalidSocialToken("알 수 없는 계정 정보입니다.", 111),
    AlreadySocialEmail("이미 가입된 계정입니다.", 112),
    AlreadyNickname("이미 등록된 닉네임입니다.", 113),

    ApplePrivateEmail("이메일 정보를 알 수 없습니다. Share My Email로 로그인해주세요.", 114),

    NotJoinUserToBee("해당 모임에 가입되지 않은 유저입니다.", 120),
    AlreadyUploadMissionToday("오늘 이미 미션을 등록했습니다.", 121),
    NotUploadTime("미션을 업로드 할 수 있는 시간이 아닙니다.", 122),

    NotCreateBee("모임 생성이 실패했습니다.", 130),

    NotJoinBee("모임 가입에 실패했습니다.", 140),

    FailWithdrwalBee("모임 탈퇴에 실패했습니다.", 150),

    CantVoteMyselfMission("자신의 미션에는 투표할 수 없습니다.", 160),
    NotJoinUser("Bee의 가입되지 않은 유저는 투표할 수 없습니다.", 161),
}