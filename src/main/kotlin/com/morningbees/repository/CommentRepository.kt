package com.morningbees.repository

import com.morningbees.model.Comment
import org.springframework.data.repository.CrudRepository

interface CommentRepository : CrudRepository<Comment, Long> {
}