package com.morningbees.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.morningbees.dto.UserInfoDto
import org.springframework.stereotype.Component
import javax.persistence.*

@Component
@Entity
@Table(name = "users")
data class User(
        @Column(unique = true)
        val nickname: String = "",

        @Column(columnDefinition = "TINYINT")
        val status: Int = UserStatus.Use.status,

        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
        val missions: MutableList<Mission> = mutableListOf()
) : BaseEntity() {
        @OneToOne(mappedBy = "user")
        val provider: UserProvider? = null

        @OneToOne(mappedBy = "user")
        val token: UserToken? = null

        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
        @JsonManagedReference
        val beePenalties: MutableList<BeePenalty> = mutableListOf()

        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
        @JsonManagedReference
        val comments: MutableList<Comment> = mutableListOf()

        @OneToMany(mappedBy = "user", cascade = [CascadeType.MERGE], fetch = FetchType.LAZY)
        val missionVotes: MutableSet<MissionVote> = mutableSetOf()

        @OneToMany(mappedBy = "user", cascade = [CascadeType.MERGE], fetch = FetchType.EAGER)
        @JsonManagedReference
        val bees: MutableSet<BeeMember> = mutableSetOf()

        fun getJoinBeeId(): Long {
                var beeId: Long = 0

                val bees = this.bees
                if(bees.isNotEmpty()) {
                        beeId = bees.last().bee.id
                }

                return beeId
        }

        fun defaultInfo(): UserInfoDto = UserInfoDto(this.id, this.nickname, "https://thragoo-test.s3.ap-northeast-2.amazonaws.com/temp_profile_image.png")

        enum class UserStatus(val status: Int) {
                Use(1),
                Withdraw(2)
        }
}
