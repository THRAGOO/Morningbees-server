package com.morningbees.model

import com.morningbees.SpringMockMvcTestSupport
import com.morningbees.repository.BeePenaltyRepository
import com.morningbees.repository.BeeRepository
import com.morningbees.repository.UserRepository
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.sql.Time

internal open class BeePenaltyTest : SpringMockMvcTestSupport() {

    @Autowired
    lateinit var beePenaltyRepository: BeePenaltyRepository

    @Autowired
    lateinit var beeRepository: BeeRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        val user = User("test")
        userRepository.save(user)

        val bee = Bee("title", "", Time(10000), 10000)
        beeRepository.save(bee)
    }

    @Test
    @FlywayTest
    @DisplayName("BeePenalty 생성에 성공한다.")
    @Transactional
    open fun successCreateBeePenalty() {
        val user = userRepository.findById(1).get()
        val bee = beeRepository.findById(1).get()

        val beePenalty = BeePenalty(bee, user, BeePenalty.BeePenaltyStatus.Pending.status)
        beePenalty.createPenalty()
        beePenaltyRepository.save(beePenalty)

        val penalty = beePenaltyRepository.findById(1).get()

        assertEquals(BeePenalty.BeePenaltyStatus.Pending.status, penalty.status)
    }
}