package com.aiang.ui.screen.calendar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.aiang.ui.theme.AIANGTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var currentDate by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedDate by remember { mutableStateOf<Date?>(null) }
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
                currentDate.add(Calendar.MONTH, -1)
                Log.i("Prev Button", "Clicked $currentDate")
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            IconButton(
                onClick = {
                    currentDate.add(Calendar.MONTH, 1)
                    Log.i("Next Button", "Clicked $currentDate")
            }) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
            }
        }

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
                    IconButton(onClick = { navController.navigate(Screen.AddTask.route) }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                }
            }

            LazyRow(
                modifier = Modifier.padding(8.dp)
            ) {
                items(dates) { date ->
                    DayItem(
                        date = date,
                        isSelected = date == selectedDate,
                        onDateSelected = { selectedDate = date },
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

fun getDatesForMonth(calendar: Calendar): List<Date> {
    val dates = mutableListOf<Date>()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    repeat(maxDay) {
        dates.add(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
    return dates
}

fun getMonthYearText(calendar: Calendar): String {
    val month = SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.time)
    val year = calendar.get(Calendar.YEAR)
    return "$month $year"
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
fun CalendarScreenPreview() {
    AIANGTheme {
        CalendarScreen(navController = rememberNavController())
    }
}