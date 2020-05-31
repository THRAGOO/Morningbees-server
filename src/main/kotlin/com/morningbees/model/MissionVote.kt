package com.morningbees.model

import javax.persistence.*

@Entity
@Table(name = "mission_votes")
data class MissionVote (
        @EmbeddedId
        private val id: MissionVoteKey?,

        @ManyToOne
        @MapsId("user_id")
        @JoinColumn(name = "user_id")
        val user: User,

        @ManyToOne
        @MapsId("mission_id")
        @JoinColumn(name = "mission_id")
        val mission: Mission,

        @Column(columnDefinition = "TINYINT")
        var type: Int = VoteType.Agree.type
) {
    constructor(user: User, mission: Mission) : this(MissionVoteKey(mission.id, user.id), user, mission)
    constructor(user: User, mission: Mission, voteType: Int) : this(MissionVoteKey(mission.id, user.id), user, mission, voteType)

    enum class VoteType(val type: Int) {
        Disagree(0),
        Agree(1)
    }
}