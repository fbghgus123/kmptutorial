package com.example.kmpfirst

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.example.kmpfirst.todo.di.todoModule
import com.example.kmpfirst.todo.presentation.TodoViewModel
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
            TodoScreen(viewModel = viewModel)
        }
    }
}