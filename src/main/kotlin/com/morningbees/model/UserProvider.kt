package com.morningbees.model

import javax.persistence.*

@Entity
@Table(name = "user_providers")
data class UserProvider(
        @OneToOne
        @JoinColumn(name = "user_id")
        val user: User,
        @Column
        val provider: String,
        @Column
        val email: String
) : BaseEntity()