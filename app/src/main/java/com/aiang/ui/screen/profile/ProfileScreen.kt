package com.aiang.ui.screen.profile

import android.widget.ToggleButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiang.R
import com.aiang.data.di.Injection
import com.aiang.data.preferences.UserModel
import com.aiang.ui.common.UiState
import com.aiang.ui.common.ViewModelFactory
import com.aiang.ui.theme.AIANGTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    var checked by remember{ mutableStateOf(false) }
    val viewModel: ProfileViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    )
    var userModel = UserModel(userId = "", email = "")

    viewModel.getSession()

    viewModel.uiState.collectAsState().value.let { uiState ->
        when(uiState) {
            is UiState.Success -> {
                userModel.email = uiState.data.email
                userModel.userId = uiState.data.userId
                userModel.name = uiState.data.name
            }
            is UiState.Error -> {}
            is UiState.Waiting -> {}
            is UiState.Loading -> {}
        }
    }

    Text(
        text = "Profile",
        textAlign = TextAlign.Left,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        modifier = Modifier.padding(16.dp)
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo_fill),
            contentDescription = null,
            modifier = Modifier
                .size(196.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Hi! ${userModel.name}",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
        )
        Text(
            text = userModel.email,
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = {}) {
            Text(
                text = "Reset Data",
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            viewModel.logout()
            viewModel.clearPreference()
        }) {
            Text(
                text = "Logout",
                fontSize = 16.sp
            )
        }
    }
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    AIANGTheme {
        ProfileScreen()
    }
}