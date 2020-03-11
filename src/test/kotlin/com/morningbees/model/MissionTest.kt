package com.morningbees.model

import com.morningbees.SpringMockMvcTestSupport
import com.morningbees.repository.BeeRepository
import com.morningbees.repository.MissionRepository
import com.morningbees.repository.UserRepository
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Time
import java.time.LocalTime
import javax.transaction.Transactional

internal open class MissionTest : SpringMockMvcTestSupport() {

    @Autowired
    lateinit var beeRepository: BeeRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var missionRepository: MissionRepository

    @BeforeEach
    fun setUp() {
        val user = User("test")
        userRepository.save(user)
        val user1 = User("test1")
        userRepository.save(user1)

        val bee = Bee("title", "", LocalTime.of(1, 0, 0), LocalTime.of(2, 0, 0), 2000)
        beeRepository.save(bee)
    }

    @Test
    @FlywayTest
    @DisplayName("Bee에 User가 미션 생성을 성공한다.")
    @Transactional
    open fun successCreateMission() {
        val user = userRepository.findById(1).get()
        val bee = beeRepository.findById(1).get()

        val mission = Mission("test.jpg",  "Test", Mission.MissionDifficulty.Intermediate, Mission.MissionType.Question, bee, user)
        missionRepository.save(mission)

        assertEquals(1, bee.missions.size)
        assertEquals("test.jpg", bee.missions.first().imageUrl)
    }

}