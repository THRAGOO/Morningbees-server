package com.morningbees.service

import com.morningbees.LogTracker
import com.morningbees.LogTrackerStub
import com.morningbees.SpringMockMvcTestSupport
import com.morningbees.dto.BeeCreateDto
import com.morningbees.model.BeeMember
import com.morningbees.model.User
import com.morningbees.repository.BeeRepository
import com.morningbees.repository.UserRepository
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.beans.factory.annotation.Autowired


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
    fun createBee() {
        val beeCreateDto = BeeCreateDto("bee", "test", 7, 10, 2000)

        val user = userRepository.findById(1).get()
        val result = beeService.create(user, beeCreateDto)

        assertEquals(result, true)
        assertEquals(user.bees.first().bee.title, "bee")
    }

    @Test
    @FlywayTest
    @DisplayName("Bee 생성에 성공하고 만든 유저가 매니저가 된다.")
    fun createBeeByManager() {
        val beeCreateDto = BeeCreateDto("bee", "test", 7, 10, 2000)

        val user = userRepository.findById(1).get()
        val result = beeService.create(user, beeCreateDto)

        assertEquals(result, true)
        assertEquals(user.bees.first().bee.title, "bee")
        assertEquals(user.bees.first().type, BeeMember.MemberType.Manager.type)
    }

}