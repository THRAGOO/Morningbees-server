package com.morningbees.model

import com.morningbees.SpringMockMvcTestSupport
import com.morningbees.repository.BeePenaltyRepository
import com.morningbees.repository.BeeRepository
import com.morningbees.repository.UserRepository
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Time

internal class BeeTest : SpringMockMvcTestSupport() {

    @Autowired
    lateinit var beeRepository: BeeRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var beePenaltyRepository: BeePenaltyRepository

    @Test
    @FlywayTest
    @DisplayName("Bee 를 생성하고 여러 유저가 해당 Bee에 가입이 성공한다.")
    fun successCreateBee() {
        val user = User("test")
        userRepository.save(user)

        val bee = Bee("title", "", Time(10000), 10000)

        bee.users.add(user)
        beeRepository.save(bee)

        assertEquals(1, bee.users.size)
        assertEquals("test", bee.users.first().nickname)
    }

    @Test
    @FlywayTest
    @DisplayName("한 모임의 두 명 이상의 유저가 가입에 성공한다.")
    fun successTwoUserJoinToBee() {
        val user = User("test")
        userRepository.save(user)
        val user1 = User("test1")
        userRepository.save(user1)

        val bee = Bee("title", "", Time(10000), 10000)

        bee.users.add(user)
        beeRepository.save(bee)
        assertEquals(1, bee.users.size)

        bee.users.add(user1)
        beeRepository.save(bee)
        assertEquals(2, bee.users.size)
    }

    @Test
    @FlywayTest
    @DisplayName("유저가 모임에서 탈퇴할 수 있어야한다.")
    fun successExitToBee() {
        val user = User("test")
        userRepository.save(user)

        val bee = Bee("title", "", Time(10000), 10000)

        bee.users.add(user)
        beeRepository.save(bee)
        assertEquals(1, bee.users.size)

        bee.users.remove(user)
        beeRepository.save(bee)
        assertEquals(0, bee.users.size)
    }
}