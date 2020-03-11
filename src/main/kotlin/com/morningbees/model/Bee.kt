
package com.morningbees.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import java.time.LocalTime
import javax.persistence.*

@Entity
@Table(name = "bees")
data class Bee(
        @Column
        val title: String,

        @Column
        val description: String,

        @Column
        val startTime: LocalTime,

        @Column
        val endTime: LocalTime,

        @Column
        val pay: Int
) : BaseEntity() {
        @OneToMany(mappedBy = "bee", cascade = [CascadeType.ALL])
        val beePenalties: MutableList<BeePenalty> = mutableListOf<BeePenalty>()

        @OneToMany(mappedBy = "bee", cascade = [CascadeType.ALL])
        val missions: MutableList<Mission> = mutableListOf<Mission>()

        @OneToMany(mappedBy = "bee", cascade = [CascadeType.MERGE])
        @JsonManagedReference
        val users: MutableSet<BeeMember> = mutableSetOf<BeeMember>()

        fun addUser(newUser: User, memberType: Int): BeeMember {
                val beeMember = BeeMember(newUser, this, memberType)
                this.users.add(beeMember)
                newUser.bees.add(beeMember)

                return beeMember
        }
        fun removeUser(user: User, memberType: Int) {
                val beeMember = BeeMember(user, this, memberType)
                this.users.remove(beeMember)
                user.bees.remove(beeMember)
        }
}