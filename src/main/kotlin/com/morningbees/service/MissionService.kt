package com.morningbees.service

import com.morningbees.dto.MissionCreateDto
import com.morningbees.dto.MissionInfoDto
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.Bee
import com.morningbees.model.Mission
import com.morningbees.model.User
import com.morningbees.repository.MissionRepository
import com.morningbees.repository.MissionRepositorySupport
import com.morningbees.util.LogEvent
import net.logstash.logback.argument.StructuredArguments.kv
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.transaction.Transactional

@Service
class MissionService(
    private val s3Service: S3Service,
    private val missionRepository: MissionRepository,
    private val missionRepositorySupport: MissionRepositorySupport,
    private val beeService: BeeService,
    private val beeMemberService: BeeMemberService
) {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    val UPLOAD_FREE_TIME: Long = 1

    //    @CacheEvict(value = ["MissionInfos"], key = "#missionCreateDto.beeId + '_' + #currentDate")
    @Transactional
    fun create(user: User, image: MultipartFile, missionCreateDto: MissionCreateDto, currentDate: String): Boolean {
        val currentTime = LocalTime.now()

        missionCreateDto.apply {
            var date = LocalDateTime.now()

            val bee: Bee = beeService.findById(beeId)
            if (!beeMemberService.isJoinUserToBee(user, bee)) throw BadRequestException("not join user", ErrorCode.NotJoinUserToBee, LogEvent.MissionServiceProcess.code, logger)
            if (alreadyUploadTargetDate(user, bee, targetDate)) throw BadRequestException("already upload mission target date", ErrorCode.AlreadyUploadMissionToday, LogEvent.MissionServiceProcess.code, logger)
            if (type == Mission.MissionType.Answer.type) {
//                if (!isTodayMission(targetDate)) throw BadRequestException("can upload today mission", ErrorCode.CanUploadTodayAnswerMission, LogEvent.MissionServiceProcess.code, logger)
//                if (bee.startTime > currentTime) throw BadRequestException("can not upload mission because start time over", ErrorCode.NotUploadTime, LogEvent.MissionServiceProcess.code, logger)
//                if (bee.endTime.plusHours(UPLOAD_FREE_TIME) < currentTime) throw BadRequestException("can not upload mission because end time over", ErrorCode.NotUploadTime, LogEvent.MissionServiceProcess.code, logger)
            }
            if (type == Mission.MissionType.Question.type) {
                date = LocalDateTime.parse("${targetDate}T00:00:00")
                if (beeService.getQuestioner(bee, date) != user)
                    throw BadRequestException("not next questioner", ErrorCode.NotNextQuestioner, LogEvent.MissionServiceProcess.code, logger)
            }

            val imageUrl: String = s3Service.upload(image)

            val mission: Mission = this.toEntity(bee, user, imageUrl, date)

            logger.info("log mission type", kv("mission_type_integer", type), kv("mission_type", mission.type))
            missionRepository.save(mission)
        }

        return true
    }

    private fun isTodayMission(targetDate: String) =
        targetDate == LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    //    @Cacheable(value = ["MissionInfos"], key = "#beeId + '_' + #targetDate.replace('-','')")
    fun fetchInfos(beeId: Long, targetDate: String): List<MissionInfoDto> {
        val bee = beeService.findById(beeId)

        return missionRepositorySupport.fetchMissionInfosByBeeAndCreatedAt(bee, targetDate)
    }

    fun alreadyUploadTargetDate(user: User, bee: Bee, targetDate: String): Boolean {
        val mission = missionRepositorySupport.findByUserAndBeeAndCreatedAt(user, bee, targetDate)
        if (mission != null) return true

        return false
    }
}