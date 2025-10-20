package com.example.kmpfirst.todo.domain.usecase

import com.example.kmpfirst.todo.domain.model.Todo
import com.example.kmpfirst.todo.domain.repository.TodoRepository

class UpdateTodoUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(todo: Todo): Result<Unit> {
        return try {
            if (todo.title.isBlank()) {
                Result.failure(IllegalArgumentException("제목을 입력해주세요"))
            } else {
                repository.updateTodo(todo)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
