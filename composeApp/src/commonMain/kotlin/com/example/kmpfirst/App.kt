package com.example.kmpfirst

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.example.kmpfirst.todo.di.todoModule
import com.example.kmpfirst.todo.presentation.TodoViewModel
import com.example.kmpfirst.todo.presentation.ui.TodoDetailScreen
import com.example.kmpfirst.todo.presentation.ui.TodoScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(todoModule)
    }) {
        MaterialTheme {
            val viewModel = koinInject<TodoViewModel>()
            var currentScreen by remember { mutableStateOf<Screen>(Screen.TodoList) }
            
            when (val screen = currentScreen) {
                is Screen.TodoList -> {
                    TodoScreen(
                        viewModel = viewModel,
                        onTodoClick = { todoId ->
                            currentScreen = Screen.TodoDetail(todoId)
                        }
                    )
                }
                is Screen.TodoDetail -> {
                    TodoDetailScreen(
                        todoId = screen.todoId,
                        viewModel = viewModel,
                        onNavigateBack = {
                            currentScreen = Screen.TodoList
                        }
                    )
                }
            }
        }
    }
}

sealed class Screen {
    data object TodoList : Screen()
    data class TodoDetail(val todoId: String) : Screen()
}
