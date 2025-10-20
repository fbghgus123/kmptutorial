package com.example.kmpfirst.todo.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.kmpfirst.todo.domain.model.Todo
import com.example.kmpfirst.todo.presentation.TodoViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
    todoId: String,
    viewModel: TodoViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val todo = uiState.todos.find { it.id == todoId }
    
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    
    if (todo == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "😕",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = "할 일을 찾을 수 없습니다",
                    style = MaterialTheme.typography.titleLarge
                )
                Button(onClick = onNavigateBack) {
                    Text("돌아가기")
                }
            }
        }
        return
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("할 일 상세", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←", style = MaterialTheme.typography.headlineMedium)
                    }
                },
                actions = {
                    IconButton(onClick = { showEditDialog = true }) {
                        Text("✏️", style = MaterialTheme.typography.titleMedium)
                    }
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Text("🗑️", style = MaterialTheme.typography.titleMedium)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 상태 카드
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (todo.isCompleted)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.secondaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                if (todo.isCompleted)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Checkbox(
                            checked = todo.isCompleted,
                            onCheckedChange = { viewModel.toggleTodo(todo.id) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color.Transparent,
                                uncheckedColor = Color.Transparent,
                                checkmarkColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (todo.isCompleted) "완료됨" else "진행중",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (todo.isCompleted)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "상태를 변경하려면 체크박스를 클릭하세요",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (todo.isCompleted)
                                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            else
                                MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            
            // 제목 섹션
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "제목",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = todo.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null,
                        color = if (todo.isCompleted)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            // 설명 섹션
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "설명",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (todo.description.isNotEmpty()) todo.description else "설명이 없습니다",
                        style = MaterialTheme.typography.bodyLarge,
                        textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null,
                        color = if (todo.description.isEmpty())
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        else if (todo.isCompleted)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            // 생성일 섹션
            if (todo.createdAt > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "생성일",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formatDate(todo.createdAt),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
    
    // 수정 다이얼로그
    if (showEditDialog) {
        EditTodoDialog(
            todo = todo,
            onDismiss = { showEditDialog = false },
            onSave = { title, description ->
                viewModel.updateTodo(todo.copy(title = title, description = description))
                showEditDialog = false
            }
        )
    }
    
    // 삭제 확인 다이얼로그
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("삭제 확인") },
            text = { Text("이 할 일을 삭제하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteTodo(todo.id)
                    showDeleteConfirm = false
                    onNavigateBack()
                }) {
                    Text("삭제", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("취소")
                }
            }
        )
    }
}

@Composable
fun EditTodoDialog(
    todo: Todo,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(todo.title) }
    var description by remember { mutableStateOf(todo.description) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("할 일 수정", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("제목") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("설명 (선택사항)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(title, description) },
                enabled = title.isNotBlank()
            ) {
                Text("저장")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

private fun formatDate(timestamp: Long): String {
    val instant = Instant.fromEpochMilliseconds(timestamp)
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${dateTime.year}년 ${dateTime.monthNumber}월 ${dateTime.dayOfMonth}일 ${dateTime.hour}:${dateTime.minute.toString().padStart(2, '0')}"
}
