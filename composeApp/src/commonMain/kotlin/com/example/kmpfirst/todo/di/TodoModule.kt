package com.example.kmpfirst.todo.di

import com.example.kmpfirst.todo.data.datasource.TodoDataSource
import com.example.kmpfirst.todo.data.repository.TodoRepositoryImpl
import com.example.kmpfirst.todo.domain.repository.TodoRepository
import com.example.kmpfirst.todo.domain.usecase.*
import com.example.kmpfirst.todo.presentation.TodoViewModel

object TodoModule {
    private val dataSource by lazy { TodoDataSource() }
    private val repository: TodoRepository by lazy { TodoRepositoryImpl(dataSource) }
    
    private val getTodosUseCase by lazy { GetTodosUseCase(repository) }
    private val addTodoUseCase by lazy { AddTodoUseCase(repository) }
    private val updateTodoUseCase by lazy { UpdateTodoUseCase(repository) }
    private val deleteTodoUseCase by lazy { DeleteTodoUseCase(repository) }
    private val toggleTodoUseCase by lazy { ToggleTodoUseCase(repository) }
    
    fun provideTodoViewModel(): TodoViewModel {
        return TodoViewModel(
            getTodosUseCase = getTodosUseCase,
            addTodoUseCase = addTodoUseCase,
            updateTodoUseCase = updateTodoUseCase,
            deleteTodoUseCase = deleteTodoUseCase,
            toggleTodoUseCase = toggleTodoUseCase
        )
    }
}
