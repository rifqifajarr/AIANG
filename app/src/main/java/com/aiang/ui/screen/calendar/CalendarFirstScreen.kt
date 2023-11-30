package com.aiang.ui.screen.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aiang.R
import com.aiang.ui.navigation.Screen
import com.aiang.ui.screen.profile.ProfileScreen
import com.aiang.ui.theme.AIANGTheme

@Composable
fun CalendarFirstScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Text(
        text = "Calendar",
        textAlign = TextAlign.Left,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        modifier = Modifier.padding(16.dp)
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(200.dp))
        Image(
            painter = painterResource(id = R.drawable.calendar_logo),
            contentDescription = null,
            modifier = Modifier
                .size(196.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "First Time?",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
        )
        Text(
            text = "You need to set your daily routine first",
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = { navController.navigate(Screen.RoutineForm.route) }) {
            Text(
                text = "Set Daily Routine",
                fontSize = 16.sp
            )
        }
    }
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
fun CalendarFirstScreenPreview() {
    AIANGTheme {
        CalendarFirstScreen(navController = rememberNavController())
    }
}