package com.example.wearable.presentation

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.wearable.presentation.theme.SpinalFreshStartWearTheme
import kotlinx.datetime.LocalDate



@Composable
fun SessionsScreen(navController: NavController) {
   SpinalFreshStartWearTheme{
        Surface (modifier = Modifier.fillMaxSize()){
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(top = 25.dp, bottom = 25.dp),
                contentAlignment = Alignment.Center
            ){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center

                ) {
                    Text(
                        text = "Sessions",
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.title1
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    val seshs = GetUserSessionData()
                    LazyColumn(modifier = Modifier.padding(5.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)){
                        items(seshs) { sesh ->
                            SessionItem(session = sesh,navController)
                        }
                    }
                }
            }

        }
    }
}
@Composable
fun SessionItem(session: Session, navController: NavController){
    Box(modifier = Modifier
        .clickable(enabled = true, onClick = { navController.navigate(Screen.Analytics.route)
        MainActivity.selectedSession = session
        })
        //.fillMaxWidth(0.7f)
        .fillMaxSize(0.7f)
        .background(Color(235 / 255f, 235 / 255f, 235 / 255f), shape = RoundedCornerShape(8.dp))
        .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
    ){
        Column(modifier = Modifier.fillMaxWidth(0.8f)
            , horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = session.name,
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.title1
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(){
                Text(
                    text = session.date.toString(),
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = formatTime(session.lifting + session.standing),
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }

}

fun GetUserSessionData():List<Session>{
    //once connected to DB will return user data but for now
    val seshs = listOf(
        Session("Session 1", LocalDate(2023, 8, 5), 235, 354),
        Session("Session 2", LocalDate(2023, 8, 4), 122, 102),
        Session("Session 3", LocalDate(2023, 8, 6), 245, 365),
        Session("Session 4", LocalDate(2023, 8, 7), 130, 110),
        Session("Session 5", LocalDate(2023, 8, 8), 255, 375),
        Session("Session 6", LocalDate(2023, 8, 9), 135, 120)

    )
    return seshs
}
class Session(
    val name: String,
    val date: LocalDate,
    val standing: Int,
    val lifting: Int
)
