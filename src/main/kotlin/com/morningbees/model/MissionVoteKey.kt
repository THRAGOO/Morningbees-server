package com.morningbees.model

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class MissionVoteKey : Serializable {

    @Column(name = "mission_id")
    val missionId: Long = 0

    @Column(name = "user_id")
    val userId: Long = 0
}