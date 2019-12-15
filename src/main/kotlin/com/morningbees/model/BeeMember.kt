package com.morningbees.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.*

@Entity
@Table(name = "bee_members")
data class BeeMember (
        @EmbeddedId
        private val id: BeeMemberKey?,

        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("user_id")
        @JoinColumn(name = "user_id")
        @JsonBackReference
        var user: User,

        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("bee_id")
        @JoinColumn(name = "bee_id")
        @JsonBackReference
        var bee: Bee,

        @Column(columnDefinition = "TINYINT")
        val type: Int = MemberType.Member.type
) {
    constructor(user: User, bee: Bee) : this(BeeMemberKey(bee.id, user.id), user, bee)

    enum class MemberType(val type: Int) {
        Owner(0),
        Member(1)
    }
}