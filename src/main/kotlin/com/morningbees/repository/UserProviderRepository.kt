package com.morningbees.repository

import com.morningbees.model.User
import com.morningbees.model.UserProvider
import org.springframework.data.repository.CrudRepository

interface UserProviderRepository : CrudRepository<UserProvider, Long> {

    fun findByUser(user: User): UserProvider
    fun existsByEmailAndProvider(email: String, provider: String): Boolean
    fun findByEmailAndProvider(email: String, provider: String): UserProvider?
}