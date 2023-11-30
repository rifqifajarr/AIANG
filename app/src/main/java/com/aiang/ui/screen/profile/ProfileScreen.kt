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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiang.R
import com.aiang.ui.theme.AIANGTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    var checked by remember{ mutableStateOf(false) }

    Text(
        text = "Profile",
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
        Spacer(modifier = Modifier.height(128.dp))
        Image(
            painter = painterResource(id = R.drawable.app_logo_fill),
            contentDescription = null,
            modifier = Modifier
                .size(196.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Hi! Nama",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
        )
        Text(
            text = "nama@mail.com",
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(36.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Dark Mode",
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.width(20.dp))
            Switch(checked = checked, onCheckedChange = { checked = it })
        }
        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = {}) {
            Text(
                text = "Reset Data",
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {}) {
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