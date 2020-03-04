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
        val type: Int = VoteType.Yes.type
) {
    constructor(user: User, mission: Mission) : this(MissionVoteKey(mission.id, user.id), user, mission)

    enum class VoteType(val type: Int) {
        No(0),
        Yes(1)
    }
}