package com.example.kmpfirst

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kmpfirst.todo.di.TodoModule
import com.example.kmpfirst.todo.presentation.ui.TodoScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel = viewModel { TodoModule.provideTodoViewModel() }
        TodoScreen(viewModel = viewModel)
    }
}