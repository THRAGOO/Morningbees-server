package com.morningbees.service.social

class NaverLoginService : SocialLoginService() {

    override val socialLoginUrl: String = "https://openapi.naver.com/v1/nid/me"

    override fun getEmailByToken(socialToken: String): String {
        return this.getResponseByUrl(socialToken)["response"]["email"].textValue()
    }
}
