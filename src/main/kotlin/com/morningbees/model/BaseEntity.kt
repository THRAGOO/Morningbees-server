package com.morningbees.model

import com.fasterxml.jackson.annotation.JsonFormat

import javax.persistence.*
import java.time.LocalDateTime

@MappedSuperclass
class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "created_at", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @PrePersist
    fun onPersist() {
        this.updatedAt = LocalDateTime.now()
        this.createdAt = this.updatedAt
    }

    @PreUpdate
    fun onUpdate() {
        this.updatedAt = LocalDateTime.now()
    }
}
