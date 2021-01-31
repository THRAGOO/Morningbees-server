package com.morningbees.service

import com.morningbees.SpringMockMvcTestSupport
import com.morningbees.dto.MissionVoteDto
import com.morningbees.exception.BadRequestException
import com.morningbees.model.*
import com.morningbees.repository.*
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalTime
import kotlin.test.assertTrue

internal class MissionVoteServiceTest : SpringMockMvcTestSupport() {
    @Autowired
    lateinit var missionVoteService: MissionVoteService
    @Autowired
    lateinit var missionVoteRepository: MissionVoteRepository
    @Autowired
    lateinit var missionRepository: MissionRepository
    @Autowired
    lateinit var beeRepository: BeeRepository
    @Autowired
    lateinit var beeMemberRepository: BeeMemberRepository
    @Autowired
    lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        val user = User("test")
        userRepository.save(user)

        val user1 = User("test1")
        userRepository.save(user1)

        val bee = Bee("title", "", LocalTime.of(1, 0, 0), LocalTime.of(2, 0, 0), 2000)
        beeRepository.save(bee)

        val beeMember = bee.addUser(user1, BeeMember.MemberType.Member.type)
        beeMemberRepository.save(beeMember)

        missionRepository.save(Mission(0, "test.jpg",  "Test", Mission.MissionDifficulty.Intermediate, Mission.MissionType.Question, bee, user))
    }

    @Test
    @FlywayTest
    fun `파라미터로 유저가 넘어오지 않으면 에러를 반환한다`() {
        val mission = missionRepository.getById(1) ?: throw Exception()
        val agreeVoteType = MissionVote.VoteType.Agree

        val missionVoteDto = MissionVoteDto(agreeVoteType.type, mission.id!!)

        val thrown = assertThrows(BadRequestException::class.java) { missionVoteService.action(null, missionVoteDto) }
        assertEquals(thrown.message, "user is null")
    }

    @Test
    @FlywayTest
    fun `파라미터로 missionVoteDto가 넘어오지 않으면 에러를 반환한다`() {
        val user = userRepository.findById(2).get()
        val thrown = assertThrows(BadRequestException::class.java) { missionVoteService.action(user, null) }
        assertEquals(thrown.message, "missionVoteDto is null")
    }

    @Test
    @FlywayTest
    fun `자신이 올린 미션에 투표하면 에러를 반환한다`() {
        val user = userRepository.findById(1).get()
        val mission = missionRepository.getById(1) ?: throw Exception()
        val agreeVoteType = MissionVote.VoteType.Agree

        val missionVoteDto = MissionVoteDto(agreeVoteType.type, mission.id!!)

        val thrown = assertThrows(BadRequestException::class.java) { missionVoteService.action(user, missionVoteDto) }
        assertEquals(thrown.message, "mission creator is not vote")
    }

    @Test
    @FlywayTest
    fun `Bee의 가입되지 않은 유저가 투표하면 에러를 반환한다`() {
        val user = User("test2")
        userRepository.save(user)
        val mission = missionRepository.getById(1) ?: throw Exception()
        val agreeVoteType = MissionVote.VoteType.Agree

        val missionVoteDto = MissionVoteDto(agreeVoteType.type, mission.id!!)

        val thrown = assertThrows(BadRequestException::class.java) { missionVoteService.action(user, missionVoteDto) }
        assertEquals(thrown.message, "not join user")
    }

    @Test
    @FlywayTest
    fun `유저가 미션의 투표 내역이 없으면 MissionVote를 생성한다`() {
        val user = userRepository.findById(2).get()
        val mission = missionRepository.getById(1) ?: throw Exception()
        val agreeVoteType = MissionVote.VoteType.Agree

        val missionVoteDto = MissionVoteDto(agreeVoteType.type, mission.id!!)

        val result = missionVoteService.action(user, missionVoteDto)
        assertTrue(result)

        val newMissionVote = missionVoteRepository.findByMissionAndUser(mission, user) ?: throw Exception()
        assertEquals(agreeVoteType.type, newMissionVote.type)
        assertEquals(mission, newMissionVote.mission)
        assertEquals(user, newMissionVote.user)
    }

    @Test
    @FlywayTest
    fun `유저가 이미 Agree로 투표했다면 Disagree로 투표할 때 상태를 업데이트한다`() {
        val user = userRepository.findById(2).get()
        userRepository.save(user)
        val mission = missionRepository.getById(1) ?: throw Exception()
        val agreeVoteType = MissionVote.VoteType.Agree

        val missionVoteDto = MissionVoteDto(agreeVoteType.type, mission.id!!)

        val result = missionVoteService.action(user, missionVoteDto)
        assertTrue(result)

        val newMissionVote = missionVoteRepository.findByMissionAndUser(mission, user) ?: throw Exception()
        assertEquals(agreeVoteType.type, newMissionVote.type)
        assertEquals(mission, newMissionVote.mission)
        assertEquals(user, newMissionVote.user)

        val disAgreeVoteType = MissionVote.VoteType.Disagree

        val newMissionVoteDto = MissionVoteDto(disAgreeVoteType.type, mission.id!!)

        val newResult = missionVoteService.action(user, newMissionVoteDto)
        assertTrue(newResult)

        val updateMissionVote = missionVoteRepository.findByMissionAndUser(mission, user) ?: throw Exception()
        assertEquals(disAgreeVoteType.type, updateMissionVote.type)
        assertEquals(mission, updateMissionVote.mission)
        assertEquals(user, updateMissionVote.user)
    }
}