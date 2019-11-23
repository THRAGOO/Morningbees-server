package com.morningbees.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "users")
data class User(
        @Column
        val nickname: String = "",

        @Column
        val status: Int = 0
) : BaseEntity()
