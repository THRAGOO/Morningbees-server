package com.morningbees.model

import javax.persistence.*

@Entity
@Table(name = "missions")
data class Mission (
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "bee_id")
        val bee: Bee,

        @OneToMany(mappedBy = "mission", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        val comment: List<Comment> = emptyList(),

        @OneToMany(mappedBy = "mission")
        val missionVotes: List<MissionVote> = emptyList(),

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
