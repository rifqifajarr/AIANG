package com.aiang

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aiang.ui.navigation.Screen
import com.aiang.ui.screen.home.HomeScaffold
import com.aiang.ui.screen.login.LoginScreen
import com.aiang.ui.screen.register.RegisterScreen
import com.aiang.ui.screen.welcome.WelcomeScreen
import com.aiang.ui.theme.AIANGTheme

@Composable
fun AiangApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route,
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen()
        }
        composable(Screen.Home.route) {
            HomeScaffold()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AiangAppPreview() {
    AIANGTheme(
        darkTheme = true
    ) {
        AiangApp()
    }
}