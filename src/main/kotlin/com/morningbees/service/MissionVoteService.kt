package com.morningbees.service

import com.morningbees.dto.MissionVoteDto
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.User
import com.morningbees.repository.MissionRepository
import com.morningbees.repository.MissionVoteRepository
import com.morningbees.util.LogEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MissionVoteService {

    @Autowired
    lateinit var missionRepository: MissionRepository
    @Autowired
    lateinit var missionVoteRepository: MissionVoteRepository
    @Autowired
    lateinit var beeMemberService: BeeMemberService

    @Transactional
    fun action(user: User?, missionVoteDto: MissionVoteDto?): Boolean {
        if (user == null) throw BadRequestException("user is null", ErrorCode.BadRequest, LogEvent.MissionVoteServiceProcess.code)
        if (missionVoteDto == null) throw BadRequestException("missionVoteDto is null", ErrorCode.BadRequest, LogEvent.MissionVoteServiceProcess.code)

        val mission = missionRepository.getById(missionVoteDto.missionId)
                ?: throw BadRequestException("misison is null", ErrorCode.BadRequest, LogEvent.MissionVoteServiceProcess.code)
        if (mission.user == user) throw BadRequestException("mission creator is not vote", ErrorCode.CantVoteMyselfMission, LogEvent.MissionVoteServiceProcess.code)
        if (!beeMemberService.isJoinUserToBee(user, mission.bee)) throw BadRequestException("not join user", ErrorCode.NotJoinUser, LogEvent.MissionVoteServiceProcess.code)

        var missionVote = missionVoteRepository.findByMissionAndUser(mission, user)

        if (missionVote == null) {
            missionVote = mission.addVote(user, missionVoteDto.voteType)
        } else {
            missionVote.type = missionVoteDto.voteType
        }

        missionVoteRepository.save(missionVote)

        return true
    }
}