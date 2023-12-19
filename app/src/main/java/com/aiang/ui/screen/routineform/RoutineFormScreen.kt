package com.aiang.ui.screen.routineform

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aiang.data.api.response.CreateActivitiesRequest
import com.aiang.ui.component.DailyRoutineForm
import com.aiang.ui.navigation.Screen
import com.aiang.ui.screen.calendar.CalendarFirstScreen
import com.aiang.ui.theme.AIANGTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiang.data.di.Injection
import com.aiang.ui.common.UiState
import com.aiang.ui.common.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineFormScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: RoutineFormViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    )
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.getSession()
        viewModel.getToken()
    }

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
                        RoutineFormContent(viewModel = viewModel)
                    }
                    is UiState.Loading -> {
                        LoadingIndicator()
                    }
                    is UiState.Success -> {
                        scope.launch {
                            snackbarHostState.showSnackbar("Routine Created!")
                        }
                        viewModel.markFormFilled()
                        navController.navigate(Screen.Calendar.route)
                    }
                    is UiState.Error -> {
                        scope.launch {
                            snackbarHostState.showSnackbar("Create Routine Failed: ${uiState.errorMessage}")
                        }
                        RoutineFormContent(viewModel = viewModel)
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
fun RoutineFormContent(
    modifier: Modifier = Modifier,
    viewModel: RoutineFormViewModel
) {

    val days = listOf(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    )

    val colors = listOf(
        Color.Cyan, Color.Magenta, Color.Blue, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary, Color.Yellow, Color.Red
    )

    val textColors = listOf(
        Color.Black, Color.White, Color.White, Color.White, Color.White, Color.Black, Color.White,
    )

    val activitiesRequests = remember {
        days.map { day ->
            CreateActivitiesRequest(day = day)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
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

            Column {
                for (index in 0..6) {
                    DailyRoutineForm(
                        color = colors[index],
                        textColor = textColors[index],
                        day = days[index],
                        onWorkStartUpdated = { activity, start ->
                            Log.i("RoutineFormScreen", "${days[index]} $activity $start")
                            activitiesRequests[index].workcoll_start = start
                        },
                        onWorkEndUpdated = { activity, end ->
                            Log.i("RoutineFormScreen", "${days[index]} $activity $end")
                            activitiesRequests[index].workcoll_end = end
                        },
                        onBreakStartUpdated = { activity, start ->
                            Log.i("RoutineFormScreen", "${days[index]} $activity $start")
                            activitiesRequests[index].break_start = start
                        },
                        onBreakEndUpdated = { activity, end ->
                            Log.i("RoutineFormScreen", "${days[index]} $activity $end")
                            activitiesRequests[index].break_end = end
                        },
                        onStudyStartUpdated = { activity, start ->
                            Log.i("RoutineFormScreen", "${days[index]} $activity $start")
                            activitiesRequests[index].studyhome_start = start
                        },
                        onStudyEndUpdated = { activity, end ->
                            Log.i("RoutineFormScreen", "${days[index]} $activity $end")
                            activitiesRequests[index].studyhome_end = end
                        },
                        onSleepStartUpdated = { activity, start ->
                            Log.i("RoutineFormScreen", "${days[index]} $activity $start")
                            activitiesRequests[index].sleep_start = start
                        },
                        onSleepEndUpdated = { activity, end ->
                            Log.i("RoutineFormScreen", "${days[index]} $activity $end")
                            activitiesRequests[index].sleep_end = end
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

//            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    for (i in 0..6) {
                        viewModel.createActivity(activitiesRequests[i])
                    }
                }
            ) {
                Text(
                    text = "Finish",
                    fontSize = 16.sp
                )
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