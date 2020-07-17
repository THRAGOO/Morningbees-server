package com.morningbees.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "bee_members")
data class BeeMember (
        @EmbeddedId
        private val id: BeeMemberKey?,

        @ManyToOne
        @MapsId("user_id")
        @JoinColumn(name = "user_id")
        @JsonBackReference
        var user: User,

        @ManyToOne
        @MapsId("bee_id")
        @JoinColumn(name = "bee_id")
        @JsonBackReference
        var bee: Bee,

        @Column(columnDefinition = "TINYINT")
        var type: Int = MemberType.Member.type
) {
    constructor(user: User, bee: Bee, memberType: Int) : this(BeeMemberKey(bee.id, user.id), user, bee, memberType)
    constructor(user: User, bee: Bee) : this(BeeMemberKey(bee.id, user.id), user, bee)

    @Column(name = "created_at", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    open var createdAt: LocalDateTime = LocalDateTime.now()

    fun isManager(): Boolean = this.type == MemberType.Manager.type

    enum class MemberType(val type: Int) {
        Manager(1),
        Member(0)
    }
}