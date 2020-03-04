package com.morningbees.model

import javax.persistence.*

@Entity
@Table(name = "comments")
data class Comment (
        @Column
        val comment: String,

        @ManyToOne
        @JoinColumn(name = "mission_id")
        val mission: Mission,

        @ManyToOne
        @JoinColumn(name = "user_id")
        val user: User
) : BaseEntity()