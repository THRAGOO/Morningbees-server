package com.morningbees.repository

import com.morningbees.model.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {

}