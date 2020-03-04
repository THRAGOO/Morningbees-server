package com.morningbees.service

import com.morningbees.model.Bee
import com.morningbees.model.User
import com.morningbees.repository.BeeMemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BeeMemeberService {

    @Autowired
    lateinit var beeMemberRepository: BeeMemberRepository

    fun isJoinUserToBee(user: User, bee: Bee): Boolean = beeMemberRepository.existsByUserAndBee(user, bee)
}