package com.aiang.ui.screen.addtask

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavHostController
import com.aiang.ui.theme.AIANGTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiang.data.di.Injection
import com.aiang.ui.common.UiState
import com.aiang.ui.common.ViewModelFactory
import com.aiang.ui.navigation.Screen
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddTaskScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: AddTaskViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    )
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)) {
            viewModel.uiState.collectAsState().value.let { uiState ->
                when (uiState) {
                    is UiState.Waiting -> {
                        AddTaskContent(viewModel = viewModel)
                    }
                    is UiState.Loading -> {
                        LoadingIndicator()
                    }
                    is UiState.Success -> {
                        scope.launch {
                            snackbarHostState.showSnackbar("Task Added!")
                        }
                        navController.navigate(Screen.Calendar.route)
                    }
                    is UiState.Error -> {
                        scope.launch {
                            snackbarHostState.showSnackbar("Create Task Failed: ${uiState.errorMessage}")
                        }
                        AddTaskContent(viewModel = viewModel)
                    }
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
@OptIn(ExperimentalMaterial3Api::class)
fun AddTaskContent(
    modifier: Modifier = Modifier,
    viewModel: AddTaskViewModel
) {
    var taskName by remember { mutableStateOf("") }
    var taskDesc by remember { mutableStateOf("") }
    var taskPriority by remember { mutableStateOf("High") }
    var taskCategory by remember { mutableStateOf("Study/Homework") }
    var selectPriority by remember { mutableStateOf(0) }
    var selectCategory by remember { mutableStateOf(0) }
    var taskDateText by remember { mutableStateOf("") }
    var taskDate by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()
    if (taskDateText == "") {
        taskDateText = getDate(calendar)
        taskDate = formatDate(calendar)
    }

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        {_: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val tempDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            taskDateText = SimpleDateFormat("EEEE, d MMM yyyy", Locale.getDefault()).format(tempDate.time)
            taskDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(tempDate.time)
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
                onClick = {
                    selectPriority = 0
                    taskPriority = "High"
                },
                label = { Text(text = "High") },
                leadingIcon = if (selectPriority == 0) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
            Spacer(modifier = Modifier.width(10.dp))
            FilterChip(
                selected = selectPriority == 1,
                onClick = {
                    selectPriority = 1
                    taskPriority = "Medium"
                },
                label = { Text(text = "Medium") },
                leadingIcon = if (selectPriority == 1) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
            Spacer(modifier = Modifier.width(10.dp))
            FilterChip(
                selected = selectPriority == 2,
                onClick = {
                    selectPriority = 2
                    taskPriority = "Low"
                },
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
                onClick = {
                    selectCategory = 0
                    taskCategory = "Study/Homework"
                },
                label = { Text(text = "Study/Homework") },
                leadingIcon = if (selectCategory == 0) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
            Spacer(modifier = Modifier.width(10.dp))
            FilterChip(
                selected = selectCategory == 1,
                onClick = {
                    selectCategory = 1
                    taskCategory = "Exercise"
                },
                label = { Text(text = "Exercise") },
                leadingIcon = if (selectCategory == 1) {
                    { Icon(imageVector = Icons.Default.Done, contentDescription = null) }
                } else { null }
            )
            Spacer(modifier = Modifier.width(10.dp))
            FilterChip(
                selected = selectCategory == 2,
                onClick = {
                    selectCategory = 2
                    taskCategory = "Fun"
                },
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
            Text(text = taskDateText, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(48.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                enabled = taskName.isNotEmpty() && taskDesc.isNotEmpty(),
                onClick = {
                    Log.i("AddTask", taskName + taskDesc + taskDate + taskCategory + taskPriority)
                    viewModel.getSession()
                    viewModel.getTokenThenAddTask(
                        taskName,
                        taskDesc,
                        taskCategory,
                        taskPriority,
                        taskDate
                    )
                }
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

fun formatDate(calendar: Calendar): String {
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    return date
}

//@Preview(device = Devices.PIXEL_4, showSystemUi = true)
//@Composable
//fun AddTaskScreenPreview() {
//    val viewModel = AddTaskViewModel()
//    AIANGTheme {
//        AddTaskContent(viewModel = viewModel)
//    }
//}