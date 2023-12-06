package com.aiang.ui.screen.routineform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.aiang.ui.component.DailyRoutineForm
import com.aiang.ui.navigation.Screen
import com.aiang.ui.screen.calendar.CalendarFirstScreen
import com.aiang.ui.theme.AIANGTheme

@Composable
fun RoutineFormScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var day by remember{ mutableStateOf(0) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Set Daily Routine",
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
            )
            Text(
                text = "Tap the 'delete' button if there is none",
                textAlign = TextAlign.Left,
                fontSize = 18.sp,
            )

            Spacer(modifier = Modifier.height(12.dp))

            when(day) {
                0 -> DailyRoutineForm(color = Color.Cyan, textColor = Color.Black, day = "Monday",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp)))
                1 -> DailyRoutineForm(color = Color.Magenta, textColor = Color.White, day = "Tuesday",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp)))
                2 -> DailyRoutineForm(color = Color.Blue, textColor = Color.White, day = "Wednesday",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp)))
                3 -> DailyRoutineForm(color = MaterialTheme.colorScheme.secondary, textColor = Color.White, day = "Thursday",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp)))
                4 -> DailyRoutineForm(color = MaterialTheme.colorScheme.primary, textColor = Color.White, day = "Friday",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp)))
                5 -> DailyRoutineForm(color = Color.Yellow, textColor = Color.Black, day = "Saturday",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp)))
                6 ->DailyRoutineForm(color = Color.Red, textColor = Color.White, day = "Sunday",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp)))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(onClick = { if(day > 0) day-- }) {
                    Text(text = "Prev")
                }
                Button(onClick = { if(day < 6) day++ }) {
                    Text(text = "Next")
                }
            }

            if (day == 6) {
                Button(onClick = { navController.navigate(Screen.Calendar.route) }) {
                    Text(
                        text = "Finish",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
fun RoutineFormScreenPreview() {
    AIANGTheme {
        RoutineFormScreen(navController = rememberNavController())
    }
}