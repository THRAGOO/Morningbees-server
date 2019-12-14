package com.morningbees.model

import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
        @Column(unique = true)
        val nickname: String = "",

        @Column(columnDefinition = "TINYINT")
        val status: Int = UserStatus.Use.status,

        @OneToMany(mappedBy = "user", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        val beePenalties: List<BeePenalty> = emptyList(),

        @OneToMany(mappedBy = "user", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        val comment: List<Comment> = emptyList(),

        @OneToMany(mappedBy = "user")
        val missionVotes: List<MissionVote> = emptyList(),

        @OneToOne(mappedBy = "user")
        val provider: UserProvider? = null,

        @OneToOne(mappedBy = "user")
        val token: UserToken? = null,

        @ManyToMany(mappedBy = "users")
        val bees: MutableList<Bee> = mutableListOf<Bee>()
) : BaseEntity() {
        enum class UserStatus(val status: Int) {
                Use(1),
                Withdraw(2)
        }
}
