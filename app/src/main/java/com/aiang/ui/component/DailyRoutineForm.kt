package com.aiang.ui.component

import android.app.TimePickerDialog
import android.widget.TimePicker
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiang.ui.theme.AIANGTheme
import java.util.Calendar
import java.util.Date

@Composable
fun DailyRoutineForm(
    modifier: Modifier = Modifier,
    color: Color,
    textColor: Color,
    day: String,
    onWorkStartUpdated: (activity: String, start: String) -> Unit,
    onWorkEndUpdated: (activity: String, end: String) -> Unit,
    onBreakStartUpdated: (activity: String, start: String) -> Unit,
    onBreakEndUpdated: (activity: String, end: String) -> Unit,
    onStudyStartUpdated: (activity: String, start: String) -> Unit,
    onStudyEndUpdated: (activity: String, end: String) -> Unit,
    onSleepStartUpdated: (activity: String, start: String) -> Unit,
    onSleepEndUpdated: (activity: String, end: String) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = day,
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(14.dp))
            FormContent(activity = "Work or College", textColor = textColor,
                onTimeStartUpdated = { start ->
                    onWorkStartUpdated("Work or College", start)
                },
                onTimeEndUpdated = { end ->
                    onWorkEndUpdated("Work or College", end)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            FormContent(activity = "Break", textColor = textColor,
                onTimeStartUpdated = { start ->
                    onBreakStartUpdated("Break", start)
                },
                onTimeEndUpdated = { end ->
                    onBreakEndUpdated("Break", end)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            FormContent(activity = "Study, Homework, or Organization", textColor = textColor,
                onTimeStartUpdated = { start ->
                    onStudyStartUpdated("Study, Homework, or Organization", start)
                },
                onTimeEndUpdated = { end ->
                    onStudyEndUpdated("Study, Homework, or Organization", end)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            FormContent(activity = "Sleep", textColor = textColor,
                onTimeStartUpdated = { start ->
                    onSleepStartUpdated("Sleep", start)
                },
                onTimeEndUpdated = { end ->
                    onSleepEndUpdated("Sleep", end)
                }
            )
        }
    }
}

@Composable
fun FormContent(
    activity: String,
    textColor: Color,
    modifier: Modifier = Modifier,
    onTimeStartUpdated: (start: String) -> Unit,
    onTimeEndUpdated: (end: String) -> Unit,
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var selectedTimeStart by remember { mutableStateOf("00:00") }
    var selectedTimeEnd by remember { mutableStateOf("00:00") }

    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    LaunchedEffect(selectedTimeStart, selectedTimeEnd) {
        onTimeStartUpdated(selectedTimeStart)
        onTimeEndUpdated(selectedTimeEnd)
    }

    val timePickerStart = TimePickerDialog(
        context,
        {_, selectedHour: Int, selectedMinute: Int ->
            var Hour: String
            var Minute: String
            if (selectedHour < 10) Hour = "0$selectedHour" else Hour = selectedHour.toString()
            if (selectedMinute < 10) Minute = "0$selectedMinute" else Minute = selectedMinute.toString()

            selectedTimeStart = "$Hour:$Minute"
        }, hour, minute, true
    )
    val timePickerEnd = TimePickerDialog(
        context,
        {_, selectedHour: Int, selectedMinute: Int ->
            var Hour: String
            var Minute: String
            if (selectedHour < 10) Hour = "0$selectedHour" else Hour = selectedHour.toString()
            if (selectedMinute < 10) Minute = "0$selectedMinute" else Minute = selectedMinute.toString()

            selectedTimeEnd = "$Hour:$Minute"
        }, hour, minute, true
    )

    var isNone by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = activity, color = textColor, fontSize = 14.sp)
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Start", color = textColor)
            if (isNone == false) {
                TextButton(
                    onClick = {
                        timePickerStart.show()
                    }
                ) {
                    Text(text = selectedTimeStart, color = textColor)
                }
            }
            Text(text = "End", color = textColor)
            if (isNone == false) {
                TextButton(
                    onClick = {
                        timePickerEnd.show()
                    }
                ) {
                    Text(text = selectedTimeEnd, color = textColor)
                }
            }
            IconButton(onClick = { isNone = !isNone }) {
                if (isNone) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = textColor)
                } else {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = textColor)
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DailyRoutineFormPreview() {
//    AIANGTheme {
//        DailyRoutineForm(color = Color.Blue, textColor = Color.White, day = "Monday")
//    }
//}