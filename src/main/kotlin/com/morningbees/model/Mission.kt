package com.morningbees.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.*

@Entity
@Table(name = "missions")
data class Mission (
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "bee_id")
        val bee: Bee,

        @OneToMany(mappedBy = "mission", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        val comment: MutableList<Comment> = mutableListOf<Comment>(),

        @OneToMany(mappedBy = "mission", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
        val missionVotes: MutableSet<MissionVote> = mutableSetOf<MissionVote>(),

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        val user: User,

        @Column
        val imageUrl: String = "",

        @Column(columnDefinition = "TINYINT")
        val type: Int = MissionType.Question.type
) : BaseEntity() {

    enum class MissionType(val type: Int) {
        Question(1),
        Answer(2)
    }
}
