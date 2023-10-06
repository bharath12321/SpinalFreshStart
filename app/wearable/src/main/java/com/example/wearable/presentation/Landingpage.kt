package com.example.wearable.presentation

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.wearable.R
import com.example.wearable.presentation.theme.SpinalFreshStartWearTheme

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun LandingViewPreview() {
    val navController = rememberSwipeDismissableNavController()
    LandingView(navController = navController)
}

@Composable
fun LandingView(navController: NavController) {
    var userLoggedIn by remember { mutableStateOf(false) }
    //userLoggedIn = ListenerService.UserData.userId



    SpinalFreshStartWearTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp), // Padding to ensure elements don't touch edges
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = ListenerService.UserData.userEmail,
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.body2
                )
                Row(
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(0.8f),

                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .height(70.dp)
                            .fillMaxWidth()
                            .padding(2.dp)
                            .align(Alignment.CenterVertically)// Add padding to the end (right side) of the Box

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.spine),
                            contentDescription = "R.mipmap.neuroflex.xml",
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .height(65.dp)
                                .padding(start = 10.dp) // Image fills the entire Box
                        )
                        Text(
                            text = "Neuroflex",
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.title1,
                            maxLines = 1,
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .align(Alignment.CenterStart) // Align the text to the start (left) of the Box
                                .padding(
                                    start = 40.dp,
                                ) // Adjust padding for overlay and spacing
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp)) // Reduced spacer size

                Button(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(vertical = 2.dp)
                        .fillMaxWidth(0.6f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    onClick = { navController.navigate(Screen.ActivityRun.route) }
                ) {
                    Text(
                        text = "Begin Session",
                        modifier = Modifier.padding(2.dp),
                        color = MaterialTheme.colors.onPrimary,
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontSize = 10.sp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp)) // Reduced spacer size

                Button(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(vertical = 2.dp)
                        .fillMaxWidth(0.6f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    onClick = { navController.navigate(Screen.Sessions.route)}
                ) {
                    Text(
                        text = "Go to Sessions",
                        modifier = Modifier.padding(2.dp),
                        color = MaterialTheme.colors.onPrimary,
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontSize = 10.sp)
                    )
                }
            }
        }
    }
}