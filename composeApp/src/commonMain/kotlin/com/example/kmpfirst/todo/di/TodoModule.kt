package com.example.kmpfirst.todo.di

import com.example.kmpfirst.todo.data.datasource.TodoDataSource
import com.example.kmpfirst.todo.data.repository.TodoRepositoryImpl
import com.example.kmpfirst.todo.domain.repository.TodoRepository
import com.example.kmpfirst.todo.domain.usecase.*
import com.example.kmpfirst.todo.presentation.TodoViewModel
import org.koin.dsl.module

val todoModule = module {
    // Data Source
    single { TodoDataSource() }
    
    // Repository
    single<TodoRepository> { TodoRepositoryImpl(get()) }
    
    // Use Cases
    factory { GetTodosUseCase(get()) }
    factory { AddTodoUseCase(get()) }
    factory { UpdateTodoUseCase(get()) }
    factory { DeleteTodoUseCase(get()) }
    factory { ToggleTodoUseCase(get()) }
    
    // ViewModel
    factory { 
        TodoViewModel(
            getTodosUseCase = get(),
            addTodoUseCase = get(),
            updateTodoUseCase = get(),
            deleteTodoUseCase = get(),
            toggleTodoUseCase = get()
        )
    }
}
