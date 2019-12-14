package com.morningbees.model

import javax.persistence.*

@Entity
@Table(name = "user_providers", uniqueConstraints = arrayOf(UniqueConstraint(name = "idx_email_provider_unique", columnNames = arrayOf("provider", "email"))))
data class UserProvider(
        @OneToOne
        @JoinColumn(name = "user_id")
        val user: User,

        @Column
        val provider: String,

        @Column
        val email: String
) : BaseEntity() {
        enum class Provider(val provider: String) {
                Google("google"),
                Naver("naver")
        }
}