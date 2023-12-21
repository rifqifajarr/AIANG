package com.aiang.ui.screen.recommendation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiang.data.api.response.RecommendationTaskResponse
import com.aiang.data.di.Injection
import com.aiang.ui.common.UiState
import com.aiang.ui.common.ViewModelFactory
import com.aiang.ui.theme.AIANGTheme

@Composable
fun RecommendationScreen(
    modifier: Modifier = Modifier,
    viewModel: RecommendationViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    taskId: String
) {
    LaunchedEffect(taskId) {
        viewModel.getSession()
        viewModel.getTokenThenGetData(taskId)
        Log.i("RecommendationScreen", "taskId $taskId")
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        viewModel.uiState.collectAsState().value.let { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    RecommendationContent(
                        stressLevel = uiState.data.stressLevel,
                        recommendation = uiState.data.recommendation,
                        taskRecommendation = uiState.data.recommendedTask
                    )
                    Log.i("RoutineRecommendation", uiState.data.recommendation)
                    Log.i("RoutineRecommendation", uiState.data.stressLevel.toString())
                    Log.i("RecommendationTask", uiState.data.recommendedTask.toString())
                }
                is UiState.Loading -> {
                    LoadingIndicator()
                }
                is UiState.Error -> {
                    Text(text = uiState.errorMessage, fontSize = 20.sp, color = Color.White)
                }
                is UiState.Waiting -> {}
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
fun RecommendationContent(
    stressLevel: Int,
    recommendation: String,
    taskRecommendation: List<RecommendationTaskResponse>
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Today's Recommendation",
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
        )
        Text(
            text = "Based on our model's analysis on your data",
            textAlign = TextAlign.Left,
            fontSize = 18.sp,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Stress Level:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stressLevel.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = stressLevel / 5f,
            color = when (stressLevel) {
                1 -> Color.Cyan
                2 -> Color.Green
                3 -> Color.Magenta
                4 -> Color.Yellow
                5 -> Color.Red
                else -> {Color.Black}
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Recommendation:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = recommendation,
            fontSize = 18.sp,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Task Recommendation:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        var i = 1
        LazyColumn {
            items(taskRecommendation) { task ->
                RecommendedTask(index = i, task = task.predictionTask)
                Spacer(modifier = Modifier.height(8.dp))
                i++
            }
        }
    }
}

@Composable
fun RecommendedTask(
    modifier: Modifier = Modifier,
    index: Int,
    task: String,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "$index. $task",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RecommendationScreenPreview() {
//    AIANGTheme {
//        RecommendationScreen(
//            stressLevel = 4,
//            recommendation = "Konseling atau Terapi: Pertimbangkan untuk berkonsultasi dengan seorang profesional kesehatan mental."
//        )
//    }
//}
