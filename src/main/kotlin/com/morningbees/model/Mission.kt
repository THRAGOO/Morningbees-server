package com.morningbees.model

import javax.persistence.*

@Entity
@Table(name = "missions")
data class Mission (
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "bee_id")
        val bee: Bee,

        @OneToMany(mappedBy = "mission", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        val comment: Set<Comment> = emptySet(),

        @OneToMany(mappedBy = "mission")
        val missionVotes: Set<MissionVote> = emptySet(),

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        val user: User,

        @Column
        val imageUrl: String = "",

        @Column
        val type: Int = MissionType.Question.type
) : BaseEntity() {

    enum class MissionType(val type: Int) {
        Question(1),
        Answer(2)
    }
}
