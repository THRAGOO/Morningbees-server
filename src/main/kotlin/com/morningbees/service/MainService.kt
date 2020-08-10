package com.morningbees.service

import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.Mission
import com.morningbees.model.User
import com.morningbees.repository.BeeRepository
import com.morningbees.util.LogEvent
import net.logstash.logback.argument.StructuredArguments
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MainService {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    lateinit var beeService: BeeService

    @Autowired
    lateinit var beeRepository: BeeRepository

    @Autowired
    lateinit var missionService: MissionService

    @Autowired
    lateinit var beeMemberService: BeeMemberService

    fun fetchMainInfo(user: User, beeId: Long, targetDate: String): HashMap<String, Any> {
        try {
            val bee = beeRepository.findById(beeId).get()
            if (!beeMemberService.isJoinUserToBee(user, bee)) throw BadRequestException("not join user", ErrorCode.NotJoinUser, LogEvent.MainServiceProcess.code)

            val missions = missionService.fetchInfos(beeId, targetDate)
            val questionMissions = missions.filter { it.type == Mission.MissionType.Question.type }

            val beeInfos = beeService.getBeeDetailInfo(bee, questionMissions.last())

            val mainInfos = HashMap<String, Any>()
            mainInfos["beeInfos"] = beeInfos
            mainInfos["missions"] = missions

            return mainInfos
        } catch(ex: Exception) {
            logger.warn(ex.message, StructuredArguments.kv("userId", user.id), StructuredArguments.kv("beeId", beeId), StructuredArguments.kv("eventCode", LogEvent.MainServiceProcess.code))
            return HashMap()
        }
    }
}