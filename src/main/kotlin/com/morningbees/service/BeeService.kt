package com.morningbees.service

import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.Bee
import com.morningbees.model.BeeMember
import com.morningbees.model.BeeMemberKey
import com.morningbees.model.User
import com.morningbees.repository.BeeRepository
import com.morningbees.repository.BeeMemberRepository
import com.morningbees.util.LogEvent
import com.sun.istack.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BeeService {

    @Autowired
    lateinit var beeRepository : BeeRepository
    @Autowired
    lateinit var beeMemberRepository : BeeMemberRepository
    @Autowired
    lateinit var userTokenService: AuthService

    fun createBeeByMaster(@NotNull description: String, @NotNull title: String, @NotNull time: String, @NotNull pay: Int):Bee {

        var bee = Bee(description = description, title = title, time = time, pay = pay)
        beeRepository.save(bee)

        return bee
    }

    fun joinBeeForMember() {

    }

}