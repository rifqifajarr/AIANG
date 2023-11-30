package com.aiang.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object CalendarFirst : Screen("calendar_first")
    object RoutineForm : Screen("routine_form")
}
