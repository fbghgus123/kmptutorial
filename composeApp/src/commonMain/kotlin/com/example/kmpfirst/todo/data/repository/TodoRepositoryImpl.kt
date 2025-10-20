package com.example.kmpfirst.todo.data.repository

import com.example.kmpfirst.todo.data.datasource.TodoDataSource
import com.example.kmpfirst.todo.domain.model.Todo
import com.example.kmpfirst.todo.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class TodoRepositoryImpl(
    private val dataSource: TodoDataSource
) : TodoRepository {
    override fun getTodos(): Flow<List<Todo>> {
        return dataSource.getTodos()
    }
    
    override suspend fun getTodoById(id: String): Todo? {
        return dataSource.getTodoById(id)
    }
    
    override suspend fun addTodo(todo: Todo) {
        dataSource.addTodo(todo)
    }
    
    override suspend fun updateTodo(todo: Todo) {
        dataSource.updateTodo(todo)
    }
    
    override suspend fun deleteTodo(id: String) {
        dataSource.deleteTodo(id)
    }
}
