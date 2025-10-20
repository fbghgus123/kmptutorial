package com.example.kmpfirst.todo.domain.usecase

import com.example.kmpfirst.todo.domain.repository.TodoRepository

class DeleteTodoUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return try {
            repository.deleteTodo(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
