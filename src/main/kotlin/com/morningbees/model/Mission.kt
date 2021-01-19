package com.morningbees.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "missions")
data class Mission (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column
    val imageUrl: String = "",

    @Column
    val description: String = "",

    @Column(columnDefinition = "TINYINT")
    val type: Int = MissionType.Question.type,

    @Column(columnDefinition = "TINYINT")
    val difficulty: Int = MissionDifficulty.Intermediate.level,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bee_id")
    val bee: Bee,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "created_at", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    var createdAt: LocalDateTime = LocalDateTime.now()
) {
    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    constructor(id: Long, imageUrl: String, description: String, difficulty: MissionDifficulty, missionType: MissionType, bee: Bee, user: User) :
            this(id, imageUrl, description, difficulty.level, missionType.type, bee, user) {
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
