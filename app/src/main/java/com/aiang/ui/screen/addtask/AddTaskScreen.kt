package com.aiang.ui.screen.addtask

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiang.ui.theme.AIANGTheme
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddTaskScreen(
    modifier: Modifier = Modifier
) {
    var taskName by remember { mutableStateOf("") }
    var taskDesc by remember { mutableStateOf("") }
    var selectPriority by remember { mutableStateOf(0) }
    var selectCategory by remember { mutableStateOf(0) }
    var taskDate by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()
    if (taskDate == "") {
        taskDate = getDate(calendar)
    }

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        {_: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val tempDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            taskDate = SimpleDateFormat("EEEE, d MMM yyyy", Locale.getDefault()).format(tempDate.time)
//            taskDate = "$dayOfMonth/${month+1}/$year"
        }, year, month, day
    )

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Add New Task",
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
        )
        Text(
            text = "Cause they keep adding up aren't they?",
            textAlign = TextAlign.Left,
            fontSize = 18.sp,
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = taskName,
            onValueChange = { taskName = it },
            label = { Text(text = "Task Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = taskDesc,
            onValueChange = { taskDesc = it },
            label = { Text(text = "Description") },
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(232.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Priority",
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            FilterChip(
                selected = selectPriority == 0,
                onClick = { selectPriority = 0 },
                label = { Text(text = "High") },
                leadingIcon = if (selectPriority == 0) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
            Spacer(modifier = Modifier.width(10.dp))
            FilterChip(
                selected = selectPriority == 1,
                onClick = { selectPriority = 1 },
                label = { Text(text = "Medium") },
                leadingIcon = if (selectPriority == 1) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
            Spacer(modifier = Modifier.width(10.dp))
            FilterChip(
                selected = selectPriority == 2,
                onClick = { selectPriority = 2 },
                label = { Text(text = "Low") },
                leadingIcon = if (selectPriority == 2) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Category",
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            FilterChip(
                selected = selectCategory == 0,
                onClick = { selectCategory = 0 },
                label = { Text(text = "Study/Homework") },
                leadingIcon = if (selectCategory == 0) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
            Spacer(modifier = Modifier.width(10.dp))
            FilterChip(
                selected = selectCategory == 1,
                onClick = { selectCategory = 1 },
                label = { Text(text = "Exercise") },
                leadingIcon = if (selectCategory == 1) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
            Spacer(modifier = Modifier.width(10.dp))
            FilterChip(
                selected = selectCategory == 2,
                onClick = { selectCategory = 2 },
                label = { Text(text = "Fun") },
                leadingIcon = if (selectCategory == 2) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Date",
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
        TextButton(onClick = { datePickerDialog.show() }) {
            Text(text = taskDate, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(48.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {  }
            ) {
                Text("Add Task", fontSize = 20.sp)
            }
        }
    }
}

fun getDate(calendar: Calendar): String {
    val day = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
    val date = calendar.get(Calendar.DAY_OF_MONTH)
    val month = SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.time)
    val year = calendar.get(Calendar.YEAR)
    return "$day, $date $month $year"
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
fun AddTaskScreenPreview() {
    AIANGTheme {
        AddTaskScreen()
    }
}