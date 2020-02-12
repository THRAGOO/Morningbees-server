package com.morningbees.model

import com.morningbees.SpringMockMvcTestSupport
import com.morningbees.repository.BeeMemberRepository
import com.morningbees.repository.BeeRepository
import com.morningbees.repository.UserRepository
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Time

internal open class BeeTest : SpringMockMvcTestSupport() {

    @Autowired
    lateinit var beeRepository: BeeRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var beeMemberRepository: BeeMemberRepository

    @BeforeEach
    fun setUp() {
        val user = User("test")
        userRepository.save(user)
        val user1 = User("test1")
        userRepository.save(user1)

        val bee = Bee("title", "", "10000", 10000)
        beeRepository.save(bee)
    }

    @Test
    @FlywayTest
    @DisplayName("Bee 를 생성하고 여러 유저가 해당 Bee에 가입이 성공한다.")
    fun successCreateBee() {
        val user = userRepository.findById(1).get()
        val bee = beeRepository.findById(1).get()

        bee.addUser(user)
        beeRepository.save(bee)

        assertEquals(1, bee.users.size)
        assertEquals("test", bee.users.first().user.nickname)
    }

    @Test
    @FlywayTest
    @DisplayName("한 모임의 두 명 이상의 유저가 가입에 성공한다.")
    fun successTwoUserJoinToBee() {
        val user1 = userRepository.findById(1).get()
        val user2 = userRepository.findById(2).get()
        val bee = beeRepository.findById(1).get()

        bee.addUser(user1)
        beeRepository.save(bee)
        assertEquals(1, bee.users.size)

        bee.addUser(user2)
        beeRepository.save(bee)
        assertEquals(2, bee.users.size)
    }

    @Test
    @FlywayTest
    @DisplayName("유저가 모임에서 탈퇴할 수 있어야한다.")
    fun successExitToBee() {
        val user = userRepository.findById(1).get()
        val bee = beeRepository.findById(1).get()

        bee.addUser(user)
        beeRepository.save(bee)
        assertEquals(1, bee.users.size)

        bee.removeUser(user)
        beeMemberRepository.deleteByBeeAndUser(bee, user)
        assertEquals(0, bee.users.size)
    }
}