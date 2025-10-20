package com.example.kmpfirst.todo.domain.repository

import com.example.kmpfirst.todo.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodos(): Flow<List<Todo>>
    suspend fun getTodoById(id: String): Todo?
    suspend fun addTodo(todo: Todo)
    suspend fun updateTodo(todo: Todo)
    suspend fun deleteTodo(id: String)
}
