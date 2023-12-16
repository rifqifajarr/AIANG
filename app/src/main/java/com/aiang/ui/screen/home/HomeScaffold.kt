package com.aiang.ui.screen.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aiang.R
import com.aiang.ui.navigation.Screen
import com.aiang.ui.screen.addtask.AddTaskScreen
import com.aiang.ui.screen.calendar.CalendarFirstScreen
import com.aiang.ui.screen.calendar.CalendarScreen
import com.aiang.ui.screen.profile.ProfileScreen
import com.aiang.ui.screen.routineform.RoutineFormScreen
import com.aiang.ui.theme.AIANGTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var isFormFilled by remember{ mutableStateOf(true) }

    Scaffold (
        bottomBar = {
            if (currentRoute != Screen.RoutineForm.route && currentRoute != Screen.AddTask.route) {
                Log.i("bottomBar", "false")
                BottomBar(navController = navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Profile.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(Screen.Calendar.route) {
                if (isFormFilled == false) {
                    CalendarFirstScreen(navController = navController)
                } else {
                    CalendarScreen(navController = navController)
                }
            }
            composable(Screen.RoutineForm.route) {
                RoutineFormScreen(navController = navController)
                isFormFilled = true
            }
            composable(Screen.AddTask.route) {
                AddTaskScreen(navController = navController)
            }
        }
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var selectedTab by remember { mutableStateOf(0) }

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.tertiary
    ) {
        BottomNavigationItem(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            icon = { Image(painter = painterResource(id = R.drawable.focus_outlined), contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Focus Zone", fontSize = 10.sp, color = Color.White) }
        )
        BottomNavigationItem(
            selected = selectedTab == 1,
            onClick = {
                selectedTab = 1
                navController.navigate(Screen.Calendar.route)
            },
            icon = { Image(painter = painterResource(id = R.drawable.calendar), contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Calendar", fontSize = 10.sp, color = Color.White) }
        )
        BottomNavigationItem(
            selected = selectedTab == 2,
            onClick = {
                selectedTab = 2
                navController.navigate(Screen.Profile.route)
            },
            icon = { Image(painter = painterResource(id = R.drawable.profile), contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Profile", fontSize = 10.sp, color = Color.White) }
        )
    }
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    AIANGTheme {
        HomeScaffold()
    }
}