package com.example.kmpfirst.todo.domain.usecase

import com.example.kmpfirst.todo.domain.model.Todo
import com.example.kmpfirst.todo.domain.repository.TodoRepository

class AddTodoUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(title: String, description: String = ""): Result<Unit> {
        return try {
            if (title.isBlank()) {
                Result.failure(IllegalArgumentException("제목을 입력해주세요"))
            } else {
                val todo = Todo(
                    id = generateId(),
                    title = title.trim(),
                    description = description.trim()
                )
                repository.addTodo(todo)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun generateId(): String {
        return "todo-${(0..999999999).random()}"
    }
}
