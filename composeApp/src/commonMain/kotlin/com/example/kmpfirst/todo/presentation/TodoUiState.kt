package com.example.kmpfirst.todo.presentation

import com.example.kmpfirst.todo.domain.model.Todo

data class TodoUiState(
    val todos: List<Todo> = emptyList(),
    val filter: TodoFilter = TodoFilter.ALL,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

enum class TodoFilter {
    ALL, ACTIVE, COMPLETED
}

val TodoUiState.filteredTodos: List<Todo>
    get() = when (filter) {
        TodoFilter.ALL -> todos
        TodoFilter.ACTIVE -> todos.filter { !it.isCompleted }
        TodoFilter.COMPLETED -> todos.filter { it.isCompleted }
    }

val TodoUiState.activeCount: Int
    get() = todos.count { !it.isCompleted }

val TodoUiState.completedCount: Int
    get() = todos.count { it.isCompleted }
