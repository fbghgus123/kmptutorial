package com.example.kmpfirst.todo.domain.usecase

import com.example.kmpfirst.todo.domain.model.Todo
import com.example.kmpfirst.todo.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class GetTodosUseCase(
    private val repository: TodoRepository
) {
    operator fun invoke(): Flow<List<Todo>> {
        return repository.getTodos()
    }
}
