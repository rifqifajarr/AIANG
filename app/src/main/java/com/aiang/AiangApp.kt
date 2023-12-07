package com.aiang

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiang.data.di.Injection
import com.aiang.ui.common.UiState
import com.aiang.ui.common.ViewModelFactory

@Composable
fun AiangApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    )
) {

    viewModel.getSession()
    viewModel.uiState.collectAsState().value.let { uiState ->
        when (uiState) {
            is UiState.Success -> {
                val isLogin = uiState.data.isLogin
                if (isLogin) {
                    HomeScaffold()
                } else {
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
                            RegisterScreen(navController = navController)
                        }
                        composable(Screen.Home.route) {
                            HomeScaffold()
                        }
                    }
                }
            }
            is UiState.Error -> {}
            is UiState.Waiting -> {}
            is UiState.Loading -> {}
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