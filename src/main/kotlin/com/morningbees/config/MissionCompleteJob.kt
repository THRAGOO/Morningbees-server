package com.morningbees.config

import com.morningbees.service.MissionCompleteService
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.context.annotation.Configuration

@Configuration
class MissionCompleteJob(
    private val missionCompleteService: MissionCompleteService
): Job {

    override fun execute(context: JobExecutionContext?) {
        println("[Collect] mission Complete Job Start...")
        missionCompleteService.complete()
    }
}