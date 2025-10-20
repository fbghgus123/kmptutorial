package com.example.kmpfirst.todo.domain.model

data class Todo(
    val id: String,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = 0L
)
