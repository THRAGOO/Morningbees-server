package com.morningbees.model

import java.sql.Time
import javax.persistence.*

@Entity
@Table(name = "bees")
data class Bee (
        @Column
        val title: String,

        @Column
        val description: String,

        @Column
        val time: Time,

        @Column
        val pay: Int,

        @OneToMany(mappedBy = "bee", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        val beePenalties: MutableList<BeePenalty> = mutableListOf<BeePenalty>(),

        @ManyToMany
        @JoinTable(
        name = "bee_members",
        joinColumns = arrayOf(JoinColumn(name = "bee_id")),
        inverseJoinColumns = arrayOf(JoinColumn(name = "user_id")))
        val users: MutableList<User> = mutableListOf<User>()
) : BaseEntity()
//{
//        @OneToMany(mappedBy = "bee", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY, targetEntity = BeePenalty::class)
//        private val _beePenalties = mutableListOf<BeePenalty>()
//
//        val beePenalties = _beePenalties.toList()
//
//        fun addBeePenalty(beePenalty: BeePenalty) = this._beePenalties.add(beePenalty)
//}
