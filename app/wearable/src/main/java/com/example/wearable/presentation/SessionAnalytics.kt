package com.example.wearable.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.wearable.R
import com.example.wearable.presentation.theme.SpinalFreshStartWearTheme

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun AnalyticsScreenPreview() {
    val navController = rememberSwipeDismissableNavController()
    AnalyticsScreen(navController = navController)
}

@Composable
fun AnalyticsScreen(navController: NavController){
SpinalFreshStartWearTheme {
    Surface {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ){
            AnalyticsItem("Standing")
            Spacer(modifier = Modifier.height(10.dp))
            AnalyticsItem("Lifting")
        }
    }
}


}
@Composable
fun AnalyticsItem(typeOfAnalysis: String){
    var imageResId = R.drawable.spine // Default image resource ID
    var message = "No analysis available"
    if(typeOfAnalysis.contains("Lifting")){
        imageResId = R.drawable.liftingstick
        message = "Lifting Position"
    }else if(typeOfAnalysis.contains("Standing")){
        imageResId = R.drawable.standingstick
        message = "Standing Poition"
    }
    //should do above in separate function SetupVariables()

    Box(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .background(Color(235 / 255f, 235 / 255f, 235 / 255f), shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp)) // Adding black outline
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = imageResId.toString(),
                modifier = Modifier
                    .size(30.dp)
            )
            Column {
                Text(
                    text = message,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.title3
                )
                Text(
                    text = "01:26",
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }

}

