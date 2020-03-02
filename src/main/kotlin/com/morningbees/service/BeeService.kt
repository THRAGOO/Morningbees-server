package com.morningbees.service

import com.morningbees.dto.CreateBeeDto
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.Bee
import com.morningbees.model.BeeMember
import com.morningbees.model.User
import com.morningbees.repository.BeeMemberRepository
import com.morningbees.repository.BeeRepository
import com.morningbees.repository.UserRepository
import com.morningbees.util.LogEvent
import com.sun.istack.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import javax.validation.Valid

@Service
class BeeService {

    @Autowired
    lateinit var beeRepository : BeeRepository

    @Autowired
    lateinit var beeMemberRepository: BeeMemberRepository

    @Autowired
    lateinit var userRepository: UserRepository


    fun createBeeByManager(@NotNull user: User, @NotNull description: String, @NotNull title: String, @NotNull time: String, @NotNull pay: Int):Bee {

        val bee = Bee(description = description, title = title, time = time, pay = pay)
        beeRepository.save(bee)

        val beeMember = BeeMember(user = user, bee = bee, memberType = 1)
        beeMemberRepository.save(beeMember)

        bee.addUser(user, BeeMember.MemberType.Manager.type)

        return bee
    }

}