package com.morningbees.service

import com.morningbees.dto.MainInfoDto
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.Mission
import com.morningbees.model.User
import com.morningbees.util.LogEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MainService(
        private val beeService: BeeService,
        private val missionService: MissionService,
        private val beeMemberService: BeeMemberService
) {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    fun fetchMainInfo(user: User, beeId: Long, targetDate: String): MainInfoDto {
        val bee = beeService.findById(beeId)
        if (!beeMemberService.isJoinUserToBee(user, bee)) throw BadRequestException("not join user", ErrorCode.NotJoinUser, LogEvent.MainServiceProcess.code, logger)

        val missions = missionService.fetchInfos(beeId, targetDate)
        val questionMissions = missions.filter { it.type == Mission.MissionType.Question.type }

        val beeInfos = beeService.getBeeDetailInfo(bee, questionMissions.last())

        return MainInfoDto(beeInfos, missions)
    }
}