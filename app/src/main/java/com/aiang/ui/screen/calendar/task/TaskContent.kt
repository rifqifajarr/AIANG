package com.aiang.ui.screen.calendar.task

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiang.data.api.response.Task
import com.aiang.ui.component.TaskItem
import java.time.LocalDate
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiang.data.di.Injection
import com.aiang.ui.common.UiState
import com.aiang.ui.common.ViewModelFactory

@Composable
fun TaskContent(
    modifier: Modifier = Modifier,
    currentDate: LocalDate,
    viewModel: TaskViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    )
) {
    var finishedTasksId: List<String> = emptyList()
    LaunchedEffect(currentDate) {
        viewModel.getSession()
        viewModel.getTokenThenGetTask(currentDate)
        finishedTasksId = viewModel.getFinishedTasksId()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        viewModel.uiState.collectAsState().value.let { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    LazyColumn {
                        items(uiState.data) { task ->
                            var isFinished = false
                            if (finishedTasksId.contains(task.id)) isFinished = true
                            var categoryName: String = if (task.category == "Study") "Study/Homework" else task.category
                            TaskItem(
                                name = task.name,
                                priority = task.priority,
                                category = categoryName,
                                desc = task.desc,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp)),
                                taskFinished = { isFinishedTask ->
                                    isFinished = !isFinishedTask
                                    viewModel.handleTaskFinished(task.id, isFinished)
                                },
                                isTaskFinished = isFinished!!
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                is UiState.Error -> {
                    Text(
                        text = uiState.errorMessage,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
                is UiState.Waiting -> {}
                is UiState.Loading -> {
                    LoadingIndicator()
                }
            }
        }
    }


}

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.width(64.dp)
    )
}