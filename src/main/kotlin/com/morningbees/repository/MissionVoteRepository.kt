package com.morningbees.repository

import com.morningbees.model.MissionVote
import org.springframework.data.repository.CrudRepository

interface MissionVoteRepository : CrudRepository<MissionVote, Long> {
}