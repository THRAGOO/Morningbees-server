package com.morningbees.model

import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
        @Column
        val nickname: String = "",

        @Column
        val status: Int = UserStatus.Use.status,

        @OneToMany(mappedBy = "user", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        val beePenalties: Set<BeePenalty> = emptySet(),

        @OneToMany(mappedBy = "user", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        val comment: Set<Comment> = emptySet(),

        @OneToMany(mappedBy = "user")
        val missionVotes: Set<MissionVote> = emptySet(),

        @ManyToMany
        @JoinTable(
        name = "bee_members",
        joinColumns = arrayOf(JoinColumn(name = "user_id")),
        inverseJoinColumns = arrayOf(JoinColumn(name = "bee_id")))
        val bees: Set<Bee> = emptySet()
) : BaseEntity() {
        enum class UserStatus(val status: Int) {
                Use(1),
                Withdraw(2)
        }
}
