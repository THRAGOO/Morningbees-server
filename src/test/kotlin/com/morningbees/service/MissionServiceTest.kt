package com.morningbees.service

import com.morningbees.SpringMockMvcTestSupport
import com.morningbees.dto.MissionCreateDto
import com.morningbees.exception.BadRequestException
import com.morningbees.model.Bee
import com.morningbees.model.BeeMember
import com.morningbees.model.Mission
import com.morningbees.model.User
import com.morningbees.repository.BeeRepository
import com.morningbees.repository.MissionRepository
import com.morningbees.repository.UserRepository
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.transaction.Transactional

internal open class MissionServiceTest : SpringMockMvcTestSupport() {
    @Autowired
    lateinit var missionService: MissionService
    @Autowired
    lateinit var missionRepository: MissionRepository
    @Autowired
    lateinit var beeRepository: BeeRepository
    @Autowired
    lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        val user = User("test")
        userRepository.save(user)

        val bee = Bee("title", "", LocalTime.of(1, 0, 0), LocalTime.of(2, 0, 0), 2000)
        beeRepository.save(bee)
    }

    @Test
    @FlywayTest
    @DisplayName("유저가 속한 Bee에 이미 오늘자 미션을 등록했으면 true를 리턴한다.")
    @Transactional
    open fun returnTrueIfAlreadyCreateMissionToday() {
        val user = userRepository.findById(1).get()
        val bee = beeRepository.findById(1).get()
        missionRepository.save(Mission("test.jpg",  "Test", Mission.MissionDifficulty.Intermediate, Mission.MissionType.Question, bee, user))

        val result = missionService.alreadyUploadTargetDate(user, bee)

        assertEquals(result, true)
    }

    @Test
    @FlywayTest
    @DisplayName("이미 오늘 미션을 업로드했으면 에러를 리턴한다.")
    @Transactional
    open fun returnThrowAlreadyUploadToday() {
        val user = userRepository.findById(1).get()
        val bee = beeRepository.findById(1).get()
        bee.addUser(user, BeeMember.MemberType.Member.type)
        beeRepository.save(bee)

        val mission = Mission("test.jpg",  "Test", Mission.MissionDifficulty.Intermediate, Mission.MissionType.Question, bee, user)
        missionRepository.save(mission)

        val firstFile = MockMultipartFile("data", "filename.txt", "text/plain", "some xml".toByteArray())

        val missionCreateDto = MissionCreateDto( 1, "test", Mission.MissionDifficulty.Intermediate.level, Mission.MissionType.Question.type)

        val thrown = assertThrows(BadRequestException::class.java) { missionService.create(user, firstFile, missionCreateDto, "20200501") }
        assertEquals(thrown.message, "already upload mission today")
    }

    @Test
    @FlywayTest
    @DisplayName("가입되지 않은 유저가 미션을 쓸 때 에러를 반환한다.")
    @Transactional
    open fun returnThrowNotJoinUserToBee() {
        val user = userRepository.findById(1).get()

        val firstFile = MockMultipartFile("data", "filename.txt", "text/plain", "some xml".toByteArray())
        val missionCreateDto = MissionCreateDto( 1, "test", Mission.MissionDifficulty.Intermediate.level, Mission.MissionType.Question.type)

        val thrown = assertThrows(BadRequestException::class.java) { missionService.create(user, firstFile, missionCreateDto, "20200501") }
        assertEquals(thrown.message, "not join user")
    }

    @Test
    @FlywayTest
    @Transactional
    open fun `해당 Bee의 모든 Mission Info를 반환한다`() {
        val user = userRepository.findById(1).get()
        val bee = beeRepository.findById(1).get()
        missionRepository.save(Mission("test.jpg",  "description", Mission.MissionDifficulty.Intermediate, Mission.MissionType.Question, bee, user))

        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val targetDate = current.format(formatter)

        val missions = missionService.fetchInfos(1, targetDate)

        assertEquals(missions[0].imageUrl, "test.jpg")
    }
}