package com.morningbees.model

import javax.persistence.*

@Entity
@Table(name = "bee_penalties")
data class BeePenalty (
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "bee_id")
        val bee: Bee,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        val user: User,

        @Column(columnDefinition = "TINYINT")
        val status: Int = BeePenaltyStatus.Pending.status
) : BaseEntity() {
    enum class BeePenaltyStatus(val status: Int) {
        Pending(0),
        Pay(1),
        Overdue(2)
    }
}