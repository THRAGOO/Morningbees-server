package com.morningbees.repository

import com.morningbees.model.Mission
import com.morningbees.model.MissionVote
import com.morningbees.model.User
import org.springframework.data.repository.CrudRepository
import java.util.*

interface MissionVoteRepository : CrudRepository<MissionVote, Long> {

    fun findByMissionAndUser(mission: Mission, user: User): MissionVote?
}