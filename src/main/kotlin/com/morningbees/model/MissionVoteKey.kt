package com.morningbees.model

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class MissionVoteKey(
        @Column(name = "mission_id")
        private val missionId: Long? = 0,

        @Column(name = "user_id")
        private val userId: Long? = 0
) : Serializable