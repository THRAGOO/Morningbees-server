package com.morningbees.model

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*

@Entity
@Table(name = "bee_penalties")
data class BeePenalty (
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "bee_id")
        @JsonBackReference
        val bee: Bee,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        @JsonBackReference
        val user: User,

        @Column(columnDefinition = "TINYINT")
        val status: Int = BeePenaltyStatus.Pending.status,

        @Column
        var penalty: Int = 0
) : BaseEntity() {
    fun createPenalty() {
        this.bee.beePenalties.add(this)
        this.user.beePenalties.add(this)
    }

    enum class BeePenaltyStatus(val status: Int) {
        Pending(0),
        Pay(1),
        Overdue(2)
    }
}