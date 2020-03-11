package com.morningbees.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import org.springframework.stereotype.Component
import javax.persistence.*

@Component
@Entity
@Table(name = "users")
data class User(
        @Column(unique = true)
        val nickname: String = "",

        @Column(columnDefinition = "TINYINT")
        val status: Int = UserStatus.Use.status
) : BaseEntity() {
        @OneToOne(mappedBy = "user")
        val provider: UserProvider? = null

        @OneToOne(mappedBy = "user")
        val token: UserToken? = null

        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
        @JsonManagedReference
        val beePenalties: MutableList<BeePenalty> = mutableListOf<BeePenalty>()

        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
        @JsonManagedReference
        val comments: MutableList<Comment> = mutableListOf<Comment>()

        @OneToMany(mappedBy = "bee", cascade = [CascadeType.ALL])
        val missions: MutableList<Mission> = mutableListOf<Mission>()

        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
        val missionVotes: MutableSet<MissionVote> = mutableSetOf<MissionVote>()

        @OneToMany(mappedBy = "user", cascade = [CascadeType.MERGE], fetch = FetchType.EAGER)
        @JsonManagedReference
        val bees: MutableSet<BeeMember> = mutableSetOf<BeeMember>()

        enum class UserStatus(val status: Int) {
                Use(1),
                Withdraw(2)
        }
}
