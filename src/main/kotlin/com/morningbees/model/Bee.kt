package com.morningbees.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import java.sql.Time
import javax.persistence.*

@Entity
@Table(name = "bees")
data class Bee(
        @Column
        val title: String,

        @Column
        val description: String,

        @Column
        val time: Time,

        @Column
        val pay: Int
) : BaseEntity() {
        @OneToMany(mappedBy = "bee", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        val beePenalties: MutableList<BeePenalty> = mutableListOf<BeePenalty>()

        @OneToMany(mappedBy = "bee", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
        @JsonManagedReference
        val users: MutableSet<BeeMember> = mutableSetOf<BeeMember>()

        fun addUser(newUser: User) {
                val beeMember = BeeMember(newUser, this)
                this.users.add(beeMember)
                newUser.bees.add(beeMember)
        }
        fun removeUser(user: User) {
                val beeMember = BeeMember(user, this)
                this.users.remove(beeMember)
                user.bees.remove(beeMember)
        }
}
