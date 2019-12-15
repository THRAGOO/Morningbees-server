package com.morningbees.repository

import com.morningbees.model.User
import com.morningbees.model.UserToken
import org.springframework.data.repository.CrudRepository

interface UserTokenRepository : CrudRepository<UserToken, Long> {
    fun findByUser(user: User): UserToken?
}