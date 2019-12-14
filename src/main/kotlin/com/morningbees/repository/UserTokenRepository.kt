package com.morningbees.repository

import com.morningbees.model.UserToken
import org.springframework.data.repository.CrudRepository

interface UserTokenRepository : CrudRepository<UserToken, Long> {
}