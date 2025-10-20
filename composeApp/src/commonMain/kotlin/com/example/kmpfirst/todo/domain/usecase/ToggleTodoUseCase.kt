package com.example.kmpfirst.todo.domain.usecase

import com.example.kmpfirst.todo.domain.repository.TodoRepository

class ToggleTodoUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return try {
            val todo = repository.getTodoById(id)
            if (todo != null) {
                repository.updateTodo(todo.copy(isCompleted = !todo.isCompleted))
                Result.success(Unit)
            } else {
                Result.failure(IllegalArgumentException("Todo를 찾을 수 없습니다"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
