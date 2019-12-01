package com.morningbees.service.social

class GoogleLoginService : SocialLoginService() {

    override val socialLoginUrl: String = "https://www.googleapis.com/oauth2/v1/tokeninfo"

    override fun getEmailByToken(socialToken: String): String {
        return this.getResponseByUrl(socialToken)["email"].textValue()
    }
}