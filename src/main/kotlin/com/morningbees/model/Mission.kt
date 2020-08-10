package com.morningbees.model

import javax.persistence.*

@Entity
@Table(name = "missions")
data class Mission (
        @Column
        val imageUrl: String = "",

        @Column
        val description: String = "",

        @Column(columnDefinition = "TINYINT")
        val type: Int = MissionType.Question.type,

        @Column(columnDefinition = "TINYINT")
        val difficulty: Int = MissionDifficulty.Intermediate.level,

        @ManyToOne
        @JoinColumn(name = "bee_id")
        val bee: Bee,

        @ManyToOne
        @JoinColumn(name = "user_id")
        val user: User
) : BaseEntity() {
    constructor(imageUrl: String, description: String, difficulty: MissionDifficulty, missionType: MissionType, bee: Bee, user: User) :
            this(imageUrl, description, difficulty.level, missionType.type, bee, user) {
        bee.missions.add(this)
        user.missions.add(this)
    }

    @OneToMany(mappedBy = "mission", cascade = [CascadeType.ALL])
    val comment: MutableList<Comment> = mutableListOf()

    @OneToMany(mappedBy = "mission", cascade = [CascadeType.MERGE])
    val missionVotes: MutableSet<MissionVote> = mutableSetOf()

    fun addVote(tryUser: User, voteType: Int): MissionVote {
        val missionVote = MissionVote(tryUser, this, voteType)
        this.missionVotes.add(missionVote)
        tryUser.missionVotes.add(missionVote)

        return missionVote
    }

    enum class MissionType(val type: Int) {
        Question(1),
        Answer(2)
    }
    enum class MissionDifficulty(val level: Int) {
        Beginning(0),
        Intermediate(1),
        Advanced(2)
    }
}
