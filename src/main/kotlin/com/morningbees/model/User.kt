package com.morningbees.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
        @Column(unique = true)
        val nickname: String = "",

        @Column(columnDefinition = "TINYINT")
        val status: Int = UserStatus.Use.status,

        @OneToOne(mappedBy = "user")
        val provider: UserProvider? = null,

        @OneToOne(mappedBy = "user")
        val token: UserToken? = null
) : BaseEntity() {
        @OneToMany(mappedBy = "user", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        @JsonManagedReference
        val beePenalties: MutableList<BeePenalty> = mutableListOf<BeePenalty>()

        @OneToMany(mappedBy = "user", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        @JsonManagedReference
        val comment: MutableList<Comment> = mutableListOf<Comment>()

        @OneToMany(mappedBy = "user", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
        val missionVotes: MutableSet<MissionVote> = mutableSetOf<MissionVote>()

        @OneToMany(mappedBy = "user", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
        @JsonManagedReference
        val bees: MutableSet<BeeMember> = mutableSetOf<BeeMember>()

        fun addBee(newBee: Bee) {
                val beeMember = BeeMember(this, newBee)
                this.bees.add(beeMember)
                newBee.users.add(beeMember)
        }

        enum class UserStatus(val status: Int) {
                Use(1),
                Withdraw(2)
        }
}
