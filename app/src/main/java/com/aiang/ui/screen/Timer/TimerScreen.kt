package com.aiang.ui.screen.Timer

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiang.R
import com.aiang.ui.theme.AIANGTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    viewModel: TimerScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var selectedPomodoroOption by remember { mutableStateOf(PomodoroOption.POMODORO_25_5_15) }
    var longBreakInterval by remember { mutableIntStateOf(3) }
    var sessionsCompleted by remember { mutableIntStateOf(0) }
    var sessionName by remember { mutableStateOf("") }
    var isTimerRunning by remember { mutableStateOf(false) }
    var timeString by remember { mutableStateOf(viewModel.timeString) }
    val updatedIsFinished by remember { mutableStateOf(viewModel.isFinished) }

    LaunchedEffect(selectedPomodoroOption) {
        viewModel.setInitialTime(selectedPomodoroOption.workDuration.toLong())
        sessionName = "Pomodoro #${sessionsCompleted + 1}"
        snapshotFlow { viewModel.timeString }
            .collect { newTime ->
                timeString = newTime
            }

        snapshotFlow { viewModel.isFinished }
            .collect {  isFinished ->
                Log.i("onFinish", isFinished.toString())
                isTimerRunning = false
                if (sessionsCompleted % longBreakInterval == 0) {
                    viewModel.setInitialTime(selectedPomodoroOption.longBreakDuration.toLong())
                    sessionName = "Long Break"
                } else {
                    viewModel.setInitialTime(selectedPomodoroOption.shortBreakDuration.toLong())
                    sessionName = "Short Break"
                }
                sessionsCompleted++
            }
    }

    Text(
        text = "Focus Zone",
        textAlign = TextAlign.Left,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        modifier = Modifier.padding(16.dp)
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Time (minutes)",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )
        Text(
            text = "Work - Short Break - Long Break",
            fontSize = 16.sp,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            FilterChip(
                selected = selectedPomodoroOption == PomodoroOption.POMODORO_25_5_15,
                onClick = {
                    selectedPomodoroOption = PomodoroOption.POMODORO_25_5_15
                },
                label = { Text(text = "25-5-15") },
                leadingIcon = if (selectedPomodoroOption == PomodoroOption.POMODORO_25_5_15) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = selectedPomodoroOption == PomodoroOption.POMODORO_30_5_15,
                onClick = {
                    selectedPomodoroOption = PomodoroOption.POMODORO_30_5_15
                },
                label = { Text(text = "30-5-15") },
                leadingIcon = if (selectedPomodoroOption == PomodoroOption.POMODORO_30_5_15) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = selectedPomodoroOption == PomodoroOption.POMODORO_25_5_20,
                onClick = {
                    selectedPomodoroOption = PomodoroOption.POMODORO_25_5_20
                },
                label = { Text(text = "25-5-20") },
                leadingIcon = if (selectedPomodoroOption == PomodoroOption.POMODORO_25_5_20) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = selectedPomodoroOption == PomodoroOption.POMODORO_30_5_20,
                onClick = {
                    selectedPomodoroOption = PomodoroOption.POMODORO_30_5_20
                },
                label = { Text(text = "30-5-20") },
                leadingIcon = if (selectedPomodoroOption == PomodoroOption.POMODORO_30_5_20) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Long Break Interval",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )
        Text(
            text = "After this repetition, there will be a long break",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            FilterChip(
                selected = longBreakInterval == 3,
                onClick = {
                    longBreakInterval = 3
                },
                label = { Text(text = "3") },
                leadingIcon = if (longBreakInterval == 3) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = longBreakInterval == 4,
                onClick = {
                    longBreakInterval = 4
                },
                label = { Text(text = "4") },
                leadingIcon = if (longBreakInterval == 4) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = longBreakInterval == 5,
                onClick = {
                    longBreakInterval = 5
                },
                label = { Text(text = "5") },
                leadingIcon = if (longBreakInterval == 5) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = sessionName,
            fontSize = 24.sp,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (isTimerRunning) timeString else "${selectedPomodoroOption.workDuration}:00",
            fontSize = 48.sp,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (!isTimerRunning) viewModel.startTimer() else viewModel.pauseTimer()
                isTimerRunning = !isTimerRunning
            }
        ) {
            Text(
                text = if (isTimerRunning) "Reset" else "Start",
                fontSize = 22.sp
            )
        }
    }
}

enum class PomodoroOption(
    val workDuration: Int,
    val shortBreakDuration: Int,
    val longBreakDuration: Int
) {
    POMODORO_25_5_15(25, 5, 15),
    POMODORO_30_5_15(30, 5, 15),
    POMODORO_25_5_20(25, 5, 20),
    POMODORO_30_5_20(30, 5, 20)
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
fun TimerScreenPreview() {
    AIANGTheme {
        TimerScreen()
    }
}