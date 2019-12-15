package com.morningbees.model

import javax.persistence.*

@Entity
@Table(name = "user_tokens")
data class UserToken (
        @OneToOne
        @JoinColumn(name = "user_id")
        val user: User,

        @Column
        var fcmToken: String?,

        @Column
        var refreshToken: String?
) : BaseEntity()