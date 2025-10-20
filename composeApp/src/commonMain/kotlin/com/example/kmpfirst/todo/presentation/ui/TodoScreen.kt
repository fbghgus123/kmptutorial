package com.example.kmpfirst.todo.presentation.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.kmpfirst.todo.presentation.TodoFilter
import com.example.kmpfirst.todo.presentation.TodoUiState
import com.example.kmpfirst.todo.presentation.TodoViewModel
import com.example.kmpfirst.todo.presentation.activeCount
import com.example.kmpfirst.todo.presentation.completedCount
import com.example.kmpfirst.todo.presentation.filteredTodos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TodoViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "할 일 목록",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text("+ 추가", style = MaterialTheme.typography.titleMedium)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            TodoFilterChips(
                currentFilter = uiState.filter,
                activeCount = uiState.activeCount,
                completedCount = uiState.completedCount,
                onFilterChange = { viewModel.setFilter(it) }
            )
            
            TodoList(
                uiState = uiState,
                onToggle = { viewModel.toggleTodo(it) },
                onDelete = { viewModel.deleteTodo(it) }
            )
        }
    }
    
    if (showAddDialog) {
        AddTodoDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { title, description ->
                viewModel.addTodo(title, description)
                showAddDialog = false
            }
        )
    }
    
    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            // 에러 표시 후 자동으로 제거
            kotlinx.coroutines.delay(3000)
            viewModel.clearError()
        }
    }
}

@Composable
fun TodoFilterChips(
    currentFilter: TodoFilter,
    activeCount: Int,
    completedCount: Int,
    onFilterChange: (TodoFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = currentFilter == TodoFilter.ALL,
            onClick = { onFilterChange(TodoFilter.ALL) },
            label = { Text("전체") }
        )
        FilterChip(
            selected = currentFilter == TodoFilter.ACTIVE,
            onClick = { onFilterChange(TodoFilter.ACTIVE) },
            label = { Text("진행중 ($activeCount)") }
        )
        FilterChip(
            selected = currentFilter == TodoFilter.COMPLETED,
            onClick = { onFilterChange(TodoFilter.COMPLETED) },
            label = { Text("완료 ($completedCount)") }
        )
    }
}

@Composable
fun TodoList(
    uiState: TodoUiState,
    onToggle: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    if (uiState.filteredTodos.isEmpty()) {
        EmptyState(filter = uiState.filter)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = uiState.filteredTodos,
                key = { it.id }
            ) { todo ->
                TodoItem(
                    todo = todo,
                    onToggle = { onToggle(todo.id) },
                    onDelete = { onDelete(todo.id) }
                )
            }
        }
    }
}

@Composable
fun EmptyState(filter: TodoFilter) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = when (filter) {
                    TodoFilter.ALL -> "📝"
                    TodoFilter.ACTIVE -> "✅"
                    TodoFilter.COMPLETED -> "🎉"
                },
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = when (filter) {
                    TodoFilter.ALL -> "할 일이 없습니다"
                    TodoFilter.ACTIVE -> "진행중인 할 일이 없습니다"
                    TodoFilter.COMPLETED -> "완료된 할 일이 없습니다"
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TodoItem(
    todo: com.example.kmpfirst.todo.domain.model.Todo,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(300)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (todo.isCompleted)
                MaterialTheme.colorScheme.surfaceVariant
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 체크박스
            Box(
                modifier = Modifier
                    .size(32.dp)
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
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Transparent,
                        uncheckedColor = Color.Transparent,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // 텍스트
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null,
                    color = if (todo.isCompleted)
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else
                        MaterialTheme.colorScheme.onSurface
                )
                if (todo.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = todo.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null
                    )
                }
            }
            
            // 삭제 버튼
            IconButton(onClick = { showDeleteConfirm = true }) {
                Text("🗑️", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
    
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("삭제 확인") },
            text = { Text("이 할 일을 삭제하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete()
                    showDeleteConfirm = false
                }) {
                    Text("삭제")
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
fun AddTodoDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("새 할 일 추가", fontWeight = FontWeight.Bold) },
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
                onClick = { onAdd(title, description) },
                enabled = title.isNotBlank()
            ) {
                Text("추가")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}
