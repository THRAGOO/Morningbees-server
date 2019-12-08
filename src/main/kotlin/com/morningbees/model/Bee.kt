package com.morningbees.model

import java.sql.Time
import javax.persistence.*

@Entity
@Table(name = "bees")
data class Bee (
        @Column
        val title: String,

        @Column
        val time: Time,

        @Column
        val pay: Int,

        @OneToMany(mappedBy = "bee", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        val beePenalties: Set<BeePenalty> = emptySet(),

        @ManyToMany(mappedBy = "bees")
        val users: Set<User> = emptySet()
) : BaseEntity()
