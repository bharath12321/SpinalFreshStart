package com.example.wearable.presentation

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.wearable.presentation.theme.SpinalFreshStartWearTheme
import androidx.compose.material3.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.wearable.R



@Composable
fun ManageScreen(navController: NavController){
    SpinalFreshStartWearTheme {
        Surface (modifier = Modifier.fillMaxSize(),
            ) {
            Box(modifier = Modifier.fillMaxSize(0.7f),
                contentAlignment = Alignment.Center) {
                Row(verticalAlignment = Alignment.CenterVertically ,horizontalArrangement = Arrangement.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                        contentDescription = null,  // Provide appropriate content description
                        modifier = Modifier
                            .size(60.dp)
                            .clickable(enabled = true, onClick = {}),
                        contentScale = ContentScale.Fit
                    )


                    Image(
                        painter = painterResource(id = R.drawable.pause),
                        contentDescription = null,  // Provide appropriate content description
                        modifier = Modifier
                            .size(60.dp)
                            .clickable(
                                enabled = true,
                                onClick = { navController.navigate(Screen.Analytics.route) }),
                    contentScale = ContentScale.Fit
                    )
                    Image(
                        painter = painterResource(id = R.drawable.stop),
                        contentDescription = null,  // Provide appropriate content description
                        modifier = Modifier
                            .size(60.dp)
                            .clickable(
                                enabled = true,
                                onClick = { navController.navigate(Screen.Analytics.route) }),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

