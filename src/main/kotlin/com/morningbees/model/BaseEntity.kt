package com.morningbees.model

import com.fasterxml.jackson.annotation.JsonFormat

import javax.persistence.*
import java.time.LocalDateTime

@MappedSuperclass
open class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected var id: Long? = null

    @Column(name = "created_at", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    protected var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    protected var updatedAt: LocalDateTime = LocalDateTime.now()

    @PrePersist
    protected fun onPersist() {
        this.updatedAt = LocalDateTime.now()
        this.createdAt = this.updatedAt
    }

    @PreUpdate
    protected fun onUpdate() {
        this.updatedAt = LocalDateTime.now()
    }
}
