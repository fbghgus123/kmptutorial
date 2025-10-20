package com.example.kmpfirst.todo.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmpfirst.todo.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoViewModel(
    private val getTodosUseCase: GetTodosUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val toggleTodoUseCase: ToggleTodoUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()
    
    init {
        observeTodos()
    }
    
    private fun observeTodos() {
        getTodosUseCase()
            .catch { e ->
                _uiState.update { it.copy(errorMessage = e.message) }
            }
            .onEach { todos ->
                _uiState.update { it.copy(todos = todos, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }
    
    fun addTodo(title: String, description: String = "") {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            addTodoUseCase(title, description)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
        }
    }
    
    fun updateTodo(todo: com.example.kmpfirst.todo.domain.model.Todo) {
        viewModelScope.launch {
            updateTodoUseCase(todo)
                .onFailure { e ->
                    _uiState.update { it.copy(errorMessage = e.message) }
                }
        }
    }
    
    fun toggleTodo(id: String) {
        viewModelScope.launch {
            toggleTodoUseCase(id)
                .onFailure { e ->
                    _uiState.update { it.copy(errorMessage = e.message) }
                }
        }
    }
    
    fun deleteTodo(id: String) {
        viewModelScope.launch {
            deleteTodoUseCase(id)
                .onFailure { e ->
                    _uiState.update { it.copy(errorMessage = e.message) }
                }
        }
    }
    
    fun setFilter(filter: TodoFilter) {
        _uiState.update { it.copy(filter = filter) }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
