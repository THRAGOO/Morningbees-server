
package com.morningbees.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import java.time.LocalTime
import javax.persistence.*

@Entity
@Table(name = "bees")
data class Bee(
        @Column
        var title: String,

        @Column
        var description: String,

        @Column
        var startTime: LocalTime,

        @Column
        var endTime: LocalTime,

        @Column
        var pay: Int
) : BaseEntity() {
        @OneToMany(mappedBy = "bee", cascade = [CascadeType.ALL])
        val beePenalties: MutableList<BeePenalty> = mutableListOf()

        @OneToMany(mappedBy = "bee", cascade = [CascadeType.ALL])
        val missions: MutableList<Mission> = mutableListOf()

        @OneToMany(mappedBy = "bee", cascade = [CascadeType.MERGE])
        @JsonManagedReference
        val users: MutableSet<BeeMember> = mutableSetOf()

        fun update(title: String, description: String, startTime: Int, endTime: Int, pay: Int): Bee {
                this.title = title
                this.description = description
                this.startTime = LocalTime.of(startTime, 0, 0)
                this.endTime = LocalTime.of(endTime, 0, 0)
                this.pay = pay
                return this
        }
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
