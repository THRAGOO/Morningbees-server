package com.morningbees.service

import com.morningbees.dto.MissionCreateDto
import com.morningbees.dto.MissionInfoDto
import com.morningbees.exception.BadRequestException
import com.morningbees.exception.ErrorCode
import com.morningbees.model.Bee
import com.morningbees.model.Mission
import com.morningbees.model.User
import com.morningbees.repository.BeeRepository
import com.morningbees.repository.MissionRepository
import com.morningbees.repository.MissionRepositorySupport
import com.morningbees.util.LogEvent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
class MissionService {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    lateinit var s3Service: S3Service
    @Autowired
    lateinit var missionRepository: MissionRepository
    @Autowired
    lateinit var missionRepositorySupport: MissionRepositorySupport
    @Autowired
    lateinit var beeRepository: BeeRepository
    @Autowired
    lateinit var beeMemberService: BeeMemberService

    val UPLOAD_FREE_TIME: Long = 1

    @CacheEvict(value = ["MissionInfos"], key = "#missionCreateDto.beeId + '_' + #currentDate")
    fun create(user: User, image: MultipartFile, missionCreateDto: MissionCreateDto, currentDate: String): Boolean {
        val currentTime = LocalTime.now()

        val bee: Bee = beeRepository.findById(missionCreateDto.beeId).get()
        if (!beeMemberService.isJoinUserToBee(user, bee)) throw BadRequestException("not join user", ErrorCode.NotJoinUserToBee, LogEvent.MissionServiceProcess.code)
        if (alreadyUploadToday(user, bee)) throw BadRequestException("already upload mission today", ErrorCode.AlreadyUploadMissionToday, LogEvent.MissionServiceProcess.code)
        if (missionCreateDto.type == Mission.MissionType.Answer.type) {
            if (bee.startTime > currentTime) throw BadRequestException("can not upload mission because start time over", ErrorCode.NotUploadTime, LogEvent.MissionServiceProcess.code)
            if (bee.endTime.plusHours(UPLOAD_FREE_TIME) < currentTime) throw BadRequestException("can not upload mission because end time over", ErrorCode.NotUploadTime, LogEvent.MissionServiceProcess.code)
        }

        val imageUrl: String = s3Service.upload(image)

        val mission: Mission = missionCreateDto.toEntity(bee, user, imageUrl)
        missionRepository.save(mission)

        return true
    }

    @Cacheable(value = ["MissionInfos"], key = "#beeId + '_' + #targetDate.replace('-','')")
    fun fetchInfos(beeId: Long, targetDate: String): List<MissionInfoDto> {
        val bee = beeRepository.findById(beeId)
        if (bee.isPresent == false) throw BadRequestException("bee is null", ErrorCode.BadRequest, LogEvent.MissionServiceProcess.code)

        val missions = missionRepositorySupport.fetchMissionInfosByBeeAndCreatedAt(bee.get(), targetDate)

        return missions
    }

    fun alreadyUploadToday(user: User, bee: Bee): Boolean {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val todayDate = current.format(formatter)

        val mission = missionRepositorySupport.findByUserAndBeeAndCreatedAt(user, bee, todayDate)
        if (mission != null) return true

        return false
    }
}