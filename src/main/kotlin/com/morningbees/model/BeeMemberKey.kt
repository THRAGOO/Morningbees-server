package com.morningbees.model

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class BeeMemberKey (
        @Column(name = "bee_id")
        private val beeId: Long? = 0,

        @Column(name = "user_id")
        private val userId: Long? = 0
) : Serializable