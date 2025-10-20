package com.example.kmpfirst.todo.data.datasource

import com.example.kmpfirst.todo.domain.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TodoDataSource {
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    
    fun getTodos(): Flow<List<Todo>> {
        return _todos.asStateFlow()
    }
    
    fun getTodoById(id: String): Todo? {
        return _todos.value.find { it.id == id }
    }
    
    fun addTodo(todo: Todo) {
        _todos.value = _todos.value + todo
    }
    
    fun updateTodo(todo: Todo) {
        _todos.value = _todos.value.map {
            if (it.id == todo.id) todo else it
        }
    }
    
    fun deleteTodo(id: String) {
        _todos.value = _todos.value.filter { it.id != id }
    }
}
