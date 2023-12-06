package com.aiang.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DayItem(
    date: Date,
    isSelected: Boolean = false,
    onDateSelected: (Date) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.clickable {
            onDateSelected(date)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(4.dp)
        ) {
            Text(
                text = SimpleDateFormat("d", Locale.getDefault()).format(date),
                fontSize = 20.sp
            )
            Text(
                text = SimpleDateFormat("E", Locale.getDefault()).format(date),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}