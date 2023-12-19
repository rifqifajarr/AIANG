package com.aiang.ui.screen.calendar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aiang.ui.component.DayItem
import com.aiang.ui.navigation.Screen
import com.aiang.ui.screen.calendar.task.TaskContent
import com.aiang.ui.theme.AIANGTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedDate by remember { mutableStateOf<LocalDate>(currentDate) }
//    var isDateMoved by remember { mutableStateOf(false) }
//    if (!isDateMoved) selectedDate = currentDate
    val dates = getDatesForMonth(currentDate)

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Calendar - ${getMonthYearText(currentDate)}",
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                modifier = Modifier.padding(16.dp)
            )
            IconButton(
                onClick = {
                    currentDate = currentDate.minusMonths(1)
//                    isDateMoved = true
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            IconButton(
                onClick = {
                    currentDate = currentDate.plusMonths(1)
//                    isDateMoved = true
            }) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
            }
        }

        TaskContent(
            currentDate = selectedDate,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(350.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clip(RoundedCornerShape(6.dp))
                    .background(color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
                ) {
                    TextButton(onClick = { navController.navigate(Screen.AddTask.route) }) {
                        Text(text = "Get Recommendation", color = Color.White)
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clip(RoundedCornerShape(6.dp))
                        .background(color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
                ) {
                    IconButton(onClick = { navController.navigate(Screen.AddTask.route) }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                }
            }

            val lazyListState = rememberLazyListState()

            LaunchedEffect(lazyListState) {
                val index = dates.indexOfFirst { it == selectedDate }
                if (index != -1) {
                    lazyListState.scrollToItem(index)
                }
            }

            LazyRow(
                modifier = Modifier.padding(8.dp),
                state = lazyListState
            ) {
                items(dates) { date ->
                    DayItem(
                        date = date,
                        isSelected = date == selectedDate,
                        onDateSelected = {
                            selectedDate = date
                         },
                        modifier = Modifier
                            .padding(2.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clip(RoundedCornerShape(6.dp))
                            .width(56.dp)
                            .height(72.dp)
                            .background(
                                color = if (date == selectedDate) MaterialTheme.colorScheme.secondary.copy(
                                    alpha = 0.4f
                                )
                                else Color.Transparent
                            )
                    )
                }
            }
        }
    }
}

fun getDatesForMonth(calendar: LocalDate): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    var date = calendar.withDayOfMonth(1)

    while (date.month == calendar.month) {
        dates.add(date)
        date = date.plusDays(1)
    }
    return dates
}

fun getMonthYearText(calendar: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("MMM yyyy")
    return calendar.format(formatter)
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
fun CalendarScreenPreview() {
    AIANGTheme {
        CalendarScreen(navController = rememberNavController())
    }
}