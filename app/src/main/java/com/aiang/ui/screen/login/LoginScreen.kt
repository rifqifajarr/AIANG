package com.aiang.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.aiang.R
import com.aiang.ui.theme.AIANGTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiang.data.di.Injection
import com.aiang.data.preferences.UserModel
import com.aiang.ui.common.UiState
import com.aiang.ui.common.ViewModelFactory
import com.aiang.ui.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: LoginViewModel = viewModel(
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
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)) {
            viewModel.uiState.collectAsState().value.let { uiState ->
                when (uiState) {
                    is UiState.Waiting -> {
                        LoginContent(viewModel = viewModel)
                    }
                    is UiState.Loading -> {
                        LoadingIndicator()
                        LoginContent(
                            viewModel = viewModel,
                            modifier = Modifier
                                .blur(32.dp)
                        )
                    }
                    is UiState.Success -> {
                        scope.launch {
                            snackbarHostState.showSnackbar("Login Success")
                        }
                        val data = uiState.data.loginResult
                        val user = UserModel(userId = data?.userId!!, email = data.email!!, name = data.name!!, token = data.token!!)
                        viewModel.saveSession(user)
                        navController.navigate(Screen.Home.route)
                    }
                    is UiState.Error -> {
                        scope.launch {
                            snackbarHostState.showSnackbar("Login Failed: ${uiState.errorMessage}")
                        }
                        LoginContent(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel
) {
    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    var isPasswordValid by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp
        )
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
            )
            OutlinedTextField(
                value = password,
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                onValueChange = {
                    password = it
                    isPasswordValid = it.length >= 8
                },
                isError = !isPasswordValid,
                singleLine = true,
                supportingText = {
                    if (password.isNotEmpty()) {
                        if (isPasswordValid) {
                            Text("")
                        } else {
                            Text("Password Should Be Min 8 Characters")
                        }
                    }
                }
            )
        }
        Button(
            enabled = email.isNotEmpty() && password.isNotEmpty(),
            onClick = {
                viewModel.login(email, password)
            }
        ) {
            Text("Login", fontSize = 20.sp)
        }
    }
}

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.width(64.dp)
    )
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    AIANGTheme {
        LoginScreen(navController = rememberNavController())
    }
}