package com.aiang.ui.screen.calendar.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiang.data.di.Injection
import com.aiang.ui.common.UiState
import com.aiang.ui.common.ViewModelFactory
import okhttp3.internal.wait
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun RoutineContent(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    viewModel: RoutineViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    getTaskId: (id: String) -> Unit
) {
    LaunchedEffect(selectedDate) {
        viewModel.getSession()
        viewModel.getTokenThenGetActivities()
    }

    val formatter = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault())
    val day = selectedDate.format(formatter)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        viewModel.uiState.collectAsState().value.let { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    val routines = uiState.data
                    val dailyRoutine = routines.first { it.day == day }
                    getTaskId(dailyRoutine.id)
                    RoutineItem(
                        day = day,
                        workStart = dailyRoutine.workcoll_start.substring(0, 5),
                        workEnd = dailyRoutine.workcoll_end.substring(0, 5),
                        breakStart = dailyRoutine.break_start.substring(0, 5),
                        breakEnd = dailyRoutine.break_end.substring(0, 5),
                        studyStart = dailyRoutine.studyhome_start.substring(0, 5),
                        studyEnd = dailyRoutine.studyhome_end.substring(0, 5),
                        sleepStart = dailyRoutine.sleep_start.substring(0, 5),
                        sleepEnd = dailyRoutine.sleep_end.substring(0, 5),
                        textColor = Color.White,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                    )
                }
                is UiState.Waiting -> {}
                is UiState.Loading -> {
                    LoadingIndicator()
                }
                is UiState.Error -> {
                    Text(
                        text = uiState.errorMessage,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = Color.White
                    )
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

@Composable
fun RoutineItem(
    modifier: Modifier = Modifier,
    day: String,
    workStart: String,
    workEnd: String,
    breakStart: String,
    breakEnd: String,
    studyStart: String,
    studyEnd: String,
    sleepStart: String,
    sleepEnd: String,
    textColor: Color,
    color: Color
) {
    Box(
        modifier = modifier
            .background(color)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = day, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(14.dp))
            RowItem(
                activity = "Work or College",
                start = workStart,
                end = workEnd,
                textColor = textColor
            )
            Spacer(modifier = Modifier.height(14.dp))
            RowItem(
                activity = "Break",
                start = breakStart,
                end = breakEnd,
                textColor = textColor
            )
            Spacer(modifier = Modifier.height(14.dp))
            RowItem(
                activity = "Study, Homework, or Organization",
                start = studyStart,
                end = studyEnd,
                textColor = textColor
            )
            Spacer(modifier = Modifier.height(14.dp))
            RowItem(
                activity = "Sleep",
                start = sleepStart,
                end = sleepEnd,
                textColor = textColor
            )
        }
    }
}

@Composable
fun RowItem(
    activity: String,
    start: String,
    end: String,
    textColor: Color
) {
    Column {
        Text(text = activity, color = textColor, fontSize = 14.sp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Start", color = textColor)
            Text(text = start, color = textColor)

            Text(text = "End", color = textColor)
            Text(text = end, color = textColor)
        }
    }
}