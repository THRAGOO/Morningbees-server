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
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest

@Service
class BeeService {

    @Autowired
    lateinit var beeRepository : BeeRepository
    @Autowired
    lateinit var beeMemberRepository : BeeMemberRepository
    @Autowired
    lateinit var userTokenService: AuthService

    fun createBeeByManager(@NotNull description: String, @NotNull title: String, @NotNull time: String, @NotNull pay: Int):Bee {

        var bee = Bee(description = description, title = title, time = time, pay = pay)
        beeRepository.save(bee)

        return bee
    }

    fun addManager(request: HttpServletRequest) {

        val bee : Bee  = request.getAttribute("Bee") as Bee
        val user: User = request.getAttribute("user") as User

        val manager = BeeMember(user= user, bee= bee, memberType= 0)

        bee.addUser(user, manager.type)
        beeRepository.save(bee)
    }




}