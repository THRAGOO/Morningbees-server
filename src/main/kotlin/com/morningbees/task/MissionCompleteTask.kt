package com.morningbees.task

import com.morningbees.config.MissionCompleteJob
import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.CronTrigger
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.TriggerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.*

@Component
class MissionCompleteTask {

    @Bean
    fun missionCompleteJobDetail(): JobDetail {
        return JobBuilder
            .newJob()
            .ofType(MissionCompleteJob::class.java)
            .storeDurably()
            .withIdentity("mission_complete_job_detail")
            .build()
    }

    @Bean
    fun missionCompleteTrigger(missionCompleteJobDetail: JobDetail): CronTrigger {
        return TriggerBuilder
            .newTrigger()
            .forJob(missionCompleteJobDetail)
            .withIdentity("mission_complete_trigger")
            .withSchedule(
                cronSchedule("0 5 0/1 * * ?")
                    .inTimeZone(TimeZone.getTimeZone("Asia/Seoul"))
            )
            .build()
    }
}