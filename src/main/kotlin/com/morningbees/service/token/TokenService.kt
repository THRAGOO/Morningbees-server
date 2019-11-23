package com.morningbees.service.token

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*
import kotlin.collections.HashMap

open class TokenService {

    private val SALT = "secret"

    open fun getExpirationDate(): Long { return Date().getTime() }

    fun generateJWTToken(payload: HashMap<String, Any?>): String {
        val headers = HashMap<String, Any?>()
        headers.put("alg", "HS256")
        headers.put("typ", "JWT")

        val claim = HashMap<String, Any?>()
        claim.put("exp", getExpirationDate())
        claim.put("sub", "morningbees")
        claim.put("iat", Date().time/1000)
        payload.forEach{ k,v -> claim.put(k, v) }

        val jwt = Jwts.builder()
                .setHeader(headers)
                .setClaims(claim)
                .signWith(SignatureAlgorithm.HS256, this.generateKey())
                .compact()
        return jwt
    }

    private fun generateKey(): ByteArray? {
        val key: ByteArray? = SALT.toByteArray()

        return key
    }
}