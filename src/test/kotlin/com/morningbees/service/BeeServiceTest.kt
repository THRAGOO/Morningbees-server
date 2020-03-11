package com.morningbees.service

import com.morningbees.SpringMockMvcTestSupport
import com.morningbees.dto.BeeCreateDto
import com.morningbees.model.BeeMember
import com.morningbees.model.User
import com.morningbees.repository.BeeRepository
import com.morningbees.repository.UserRepository
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional
import com.morningbees.LogTracker
import com.morningbees.LogTrackerStub
import org.junit.jupiter.api.extension.RegisterExtension



internal open class BeeServiceTest : SpringMockMvcTestSupport() {

    companion object {
        @JvmField
        @RegisterExtension
        val logTrackerStub = LogTrackerStub.create().recordForLevel(LogTracker.LogLevel.WARN)
                .recordForType(BeeService::class.java)
    }

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var beeRepository: BeeRepository
    @Autowired
    lateinit var beeService: BeeService

    @BeforeEach
    fun setUp() {
        val user = User("test")
        userRepository.save(user)
    }

    @Test
    @FlywayTest
    @DisplayName("Bee 생성에 성공한다.")
    @Transactional
    open fun createBee() {
        val beeCreateDto = BeeCreateDto("bee", "test", 7, 10, 2000)

        val user = userRepository.findById(1).get()
        val result = beeService.createBeeByManager(user, beeCreateDto)

        assertEquals(result, true)
        assertEquals(user.bees.first().bee.title, "bee")
    }

    @Test
    @FlywayTest
    @DisplayName("Bee 생성에 성공하고 만든 유저가 매니저가 된다.")
    @Transactional
    open fun createBeeByManager() {
        val beeCreateDto = BeeCreateDto("bee", "test", 7, 10, 2000)

        val user = userRepository.findById(1).get()
        val result = beeService.createBeeByManager(user, beeCreateDto)

        assertEquals(result, true)
        assertEquals(user.bees.first().bee.title, "bee")
        assertEquals(user.bees.first().type, BeeMember.MemberType.Manager.type)
    }

    @Test
    @FlywayTest
    @DisplayName("startTime이 정해진 시간보다 빠르면 로그를 발생하고 false를 반환한다.")
    @Transactional
    open fun exceptionCreateBeeByStartTime() {
        val beeCreateDto = BeeCreateDto("bee", "test", 3, 10, 2000)

        val user = userRepository.findById(1).get()
        val result = beeService.createBeeByManager(user, beeCreateDto)

        assertTrue(logTrackerStub.contains("not match startTime"))
        assertEquals(result, false)
    }

    @Test
    @FlywayTest
    @DisplayName("endTime이 정해진 시간보다 늦으면 로그를 발생하고 false를 반환한다.")
    @Transactional
    open fun exceptionCreateBeeByEndTime() {
        val beeCreateDto = BeeCreateDto("bee", "test", 7, 12, 2000)

        val user = userRepository.findById(1).get()
        val result = beeService.createBeeByManager(user, beeCreateDto)

        assertTrue(logTrackerStub.contains("not match endTime"))
        assertEquals(result, false)
    }

    @Test
    @FlywayTest
    @DisplayName("pay가 정해진 가격대에 맞지 않으면 로그를 발생하고 false를 반환한다.")
    @Transactional
    open fun exceptionCreateBeeByPay() {
        val beeCreateDto = BeeCreateDto("bee", "test", 7, 10, 1000)

        val user = userRepository.findById(1).get()
        val result = beeService.createBeeByManager(user, beeCreateDto)

        assertTrue(logTrackerStub.contains("not match pay"))
        assertEquals(result, false)
    }
}