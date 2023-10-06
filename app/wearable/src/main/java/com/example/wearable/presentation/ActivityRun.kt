package com.example.wearable.presentation

import android.content.Context
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.MaterialTheme
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import com.example.wearable.R
import com.example.wearable.presentation.theme.SpinalFreshStartWearTheme


private val colorGradient = List(100) { index ->
    Color(
        red = (255 * (index / 99f)).toInt(),
        green = 255 - (255 * (index / 99f)).toInt(),
        blue = 0
    )
}
fun IfCriticalAngle(angle:Int){
    if(angle >= 80){
        //View.performHapticFeedback(HapticFeedbackConstant.)
    }
}
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun RunScreenPreview() {
    val navController = rememberSwipeDismissableNavController()
    RunScreen(navController = navController)
}



@Composable
fun RunScreen(navController: NavController) {
    SpinalFreshStartWearTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(10.dp)
        ) {
            val context = LocalContext.current

            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            var timerValue = remember{ mutableStateOf(0) }
            var angleValue = remember { mutableStateOf(20f)}
            var angleNum = 0
            var vibrate = remember { mutableStateOf(false) }

            var timerRunningState by remember { mutableStateOf(TimerState.RUNNING) }
            var backgroundColor by remember { mutableStateOf(Color.Green) }
            var manageTimer by remember {mutableStateOf(false)}


            LaunchedEffect(key1 = true){
                while(true){
                    angleValue.value = ListenerService.UserData.liveAngle!!.toFloat()
                    /*if(angleNum < Angles.nums.size - 1) {
                        angleNum += 1
                    }else{
                        angleNum = 0
                    }*/
                    delay(50L)
                }
            }
            // Simulating color change based on timer value
            LaunchedEffect(angleValue.value) {
                vibrate.value = angleValue.value >= 80
                backgroundColor = getColorForValue(angleValue.value)

            }
            LaunchedEffect(key1 = timerRunningState) {
                while (timerRunningState == TimerState.RUNNING) {
                    delay(1000L)
                        timerValue.value += 1  // Increment the timer value by 1 every second
                }
            }

            LaunchedEffect(vibrate.value){
                if(vibrate.value){

                    println("every 5 seconds")
                    if(vibrator.hasVibrator()) {
                        println("has vibrator")
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                            println("new vibration")
                                vibrator.vibrate(
                                    VibrationEffect.createOneShot(
                                        1000,
                                        VibrationEffect.DEFAULT_AMPLITUDE
                                    )
                                )
                                delay(1000L)
                            vibrator.cancel()
                        }else{
                            println("old vibration")
                            @Suppress("DEPRECATION")
                            vibrator.vibrate(1000)
                            delay(1000)
                            vibrator.cancel()
                        }
                    }//vibrator.vibrate(1000)
                }
            }
            fun playTimer(){
                timerRunningState = TimerState.RUNNING
                manageTimer = !manageTimer
                WearSender(context).sendMessage("timer","play".toByteArray())
            }
            fun pauseTimer(){
                timerRunningState = TimerState.PAUSED
                WearSender(context).sendMessage("timer","pause".toByteArray())
            }
            fun stopTimer(){
                timerRunningState = TimerState.STOPPED
                timerValue.value = 0
                manageTimer = !manageTimer
                WearSender(context).sendMessage("timer","stop".toByteArray())
            }


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Timer text
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .background(color = MaterialTheme.colors.primary)
                        .align(Alignment.CenterHorizontally)
                        .clickable(enabled = true, onClick = { manageTimer = !manageTimer })
                ) {
                    Text(
                        color = MaterialTheme.colors.onPrimary,
                        text = formatTime(timerValue.value),
                        style = MaterialTheme.typography.title1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center) // Center text within the Box
                            .fillMaxWidth()
                            .padding(5.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Large number with semi-transparent background
                if (manageTimer == true){
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {
                        Row(verticalAlignment = Alignment.CenterVertically ,horizontalArrangement = Arrangement.Center) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                                contentDescription = null,  // Provide appropriate content description
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable(enabled = true, onClick = { playTimer() }),
                                contentScale = ContentScale.Fit
                            )


                            Image(
                                painter = painterResource(id = R.drawable.pause),
                                contentDescription = null,  // Provide appropriate content description
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable(enabled = true, onClick = { pauseTimer() }),
                                contentScale = ContentScale.Fit
                            )
                            Image(
                                painter = painterResource(id = R.drawable.stop),
                                contentDescription = null,  // Provide appropriate content description
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable(enabled = true, onClick = { stopTimer() }),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }else{
                    Box(modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(90.dp)
                        .align(Alignment.CenterHorizontally)
                        .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .background(
                            color = backgroundColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        )
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = angleValue.value.toString() + "°",
                                style = MaterialTheme.typography.title2,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .padding(start = 5.dp),
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "Bend Angle",
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.End)
                                    .padding(0.dp),
                                color = MaterialTheme.colors.onBackground,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }


            }
        }
    }
}

fun getNumGradient(angle: Int): Int{
    return (angle)
}

// Function to determine background color based on the value

fun getColorForValue(value: Float): Color {
    return colorGradient[getNumGradient(value.toInt()) %100]

}

// Function to format time in MM:SS format
fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}"
}
enum class TimerState {
    RUNNING, PAUSED, STOPPED
}
object Angles {
    val nums = arrayOf(
        "0", "0.6", "0.4", "0.4", "0.4", "0.4", "0.4", "0.4", "0.4", "0.4",
        "0.4", "0.4", "0.5", "0.5", "0.4", "0.4", "0.4", "0.5", "0.5", "0.5",
        "0.4", "0.4", "0.4", "0.4", "0.4", "0.4", "0.5", "0.6", "0.8", "0.9",
        "1", "1.2", "1.5", "2", "2.3", "2.7", "3.1", "3.5", "4", "4.9",
        "5.5", "5.4", "5.5", "6", "6.6", "6.9", "6.9", "6.9", "7", "7.4",
        "7.9", "8.2", "8.3", "8.4", "8.4", "8.7", "9.3", "9.8", "10.4", "10.9",
        "11.4", "11.8", "11.9", "12.1", "12.5", "12.9", "13.2", "13.6", "14.2",
        "14.8", "15.6", "16.1", "16.4", "16.8", "17.4", "17.9", "18.7", "19.5",
        "20.4", "21.4", "22.4", "23.5", "24.7", "25.7", "26.9", "28", "29.2", "30.2",
        "31.1", "32.3", "33.7", "35.2", "36.8", "38.5", "40.3", "42.1", "43.8", "45.4",
        "46.8", "47.7", "48.2", "48.8", "49.8", "50.4", "51.5", "52.8", "54", "55.1",
        "55.8", "56.2", "56.7", "57.1", "57.6", "58.2", "58.8", "59.4", "59.8", "59.9",
        "60.2", "60.5", "60.7", "60.5", "60.5", "60.8", "60.8", "60.7", "60.5", "60.5",
        "60.7", "60.7", "60.7", "60.7", "60.7", "60.6", "60.5", "60.5", "60.6", "60.7",
        "60.6", "60.7", "60.7", "60.5", "60.4", "60.4", "60.4", "60.4", "60.1", "59.6",
        "59", "58.4", "57.6", "56.4", "54.9", "53.3", "51.6", "50", "48.3", "46.6",
        "44.7", "42.8", "40.6", "38.3", "36.5", "34.9", "33.2", "31.3", "29", "26.5",
        "24.9", "23.1", "21.9", "20.6", "18.9", "17.3", "15.6", "13.7", "12.5", "12.9",
        "13", "12.8", "12", "10.9", "9.8", "8.5", "7.5", "6.6", "5.6", "4.8",
        "4.2", "3.5", "2.7", "1.6", "0.7", "0.2", "0.1", "0.5", "1.4", "2.2",
        "2.8", "3.3", "3.7", "4.1", "4.3", "4.4", "4.2", "4.2", "4.2", "4.2",
        "4", "3.7", "3.6", "3.6", "3.6", "3.6", "3.5", "3.3", "3.1", "3",
        "3", "3", "2.7", "2.6", "2.7", "2.7", "2.7", "2.7", "2.7", "2.7",
        "2.5", "2.3", "1.9", "1.2", "0.2", "1.8", "3.1", "4.3", "5.5", "7.1",
        "8.9", "10.8", "12.9", "14.9", "17", "19.2", "21.2", "23.1", "24.5", "25.9",
        "26.9", "27.9", "28.6", "29.1", "29.2", "28.9", "28.1", "27.1", "25.8", "24.2",
        "22.3", "19.9", "17.5", "15.2", "12.9", "11.1", "9.6", "7.9", "6.2", "4.5",
        "2.3", "0.8", "1.9", "2.9", "2.9", "2.4", "2.1", "1.8", "1.6", "1.1",
        "0.7", "1.9", "3.7", "5.5", "7.2", "8.9", "10.6", "12.3", "14.5", "17.2",
        "19.8", "22.2", "24.4", "26.3", "28.3", "30.1", "32.4", "34.5", "36.6", "38.5",
        "40.5", "42.6", "44.9", "47", "48.7", "49.8", "50.8", "51.7", "52.5", "53.5",
        "54.3", "54.9", "55.5", "56", "56.3", "56.5", "56.7", "56.8", "56.7", "56.6",
        "56.4", "56.2", "56", "55.7", "55.5", "55.3", "55.2", "55", "54.8", "54.6",
        "54.5", "54.4", "54.3", "54.2", "54.2", "54.1", "54", "54", "53.9", "53.8",
        "53.7", "53.6", "53.6", "53.5", "53.4", "53.3", "53.3", "53.2", "53.1", "53",
        "53", "52.9", "52.8", "52.8", "52.7", "52.6", "52.5", "52.5", "52.4", "52.3",
        "52.2", "52.2", "52.1", "52", "51.9", "51.9", "51.8", "51.7", "51.7", "51.6",
        "51.5", "51.4", "51.4", "51.3", "51.2", "51.1", "51.1", "51", "50.9", "50.9",
        "50.8", "50.7", "50.6", "50.6", "50.5", "50.4", "50.3", "50.3", "50.2", "50.1",
        "50", "50", "49.9", "49.8", "49.7", "49.7", "49.6", "49.5", "49.4", "49.4",
        "49.3", "49.2", "49.1", "49.1", "49", "48.9", "48.9", "48.8", "48.7", "48.6",
        "48.6", "48.5", "48.4", "48.3", "48.3", "48.2", "48.1", "48", "48", "47.9",
        "47.8", "47.7", "47.7", "47.6", "47.5", "47.4", "47.4", "47.3", "47.2", "47.2",
        "47.1", "47", "46.9", "46.9", "46.8", "46.7", "46.6", "46.6", "46.5", "46.4",
        "46.3", "46.3", "46.2", "46.1", "46", "46", "45.9", "45.8", "45.7", "45.7",
        "45.6", "45.5", "45.4", "45.4", "45.3", "45.2", "45.2", "45.1", "45", "44.9",
        "44.9", "44.8", "44.7", "44.6", "44.6", "44.5", "44.4", "44.3", "44.3", "44.2",
        "44.1", "44", "44", "43.9", "43.8", "43.7", "43.7", "43.6", "43.5", "43.4",
        "43.4", "43.3", "43.2", "43.2", "43.1", "43", "42.9", "42.9", "42.8", "42.7",
        "42.6", "42.6", "42.5", "42.4", "42.3", "42.3", "42.2", "42.1", "42", "42",
        "41.9", "41.8", "41.7", "41.7", "41.6", "41.5", "41.4", "41.4", "41.3", "41.2",
        "41.2", "41.1", "41", "40.9", "40.9", "40.8", "40.7", "40.6", "40.6", "40.5",
        "40.4", "40.3", "40.3", "40.2", "40.1", "40", "40", "39.9", "39.8", "39.7",
        "39.7", "39.6", "39.5", "39.4", "39.4", "39.3", "39.2", "39.2", "39.1", "39",
        "38.9", "38.9", "38.8", "38.7", "38.6", "38.6", "38.5", "38.4", "38.3", "38.3",
        "38.2", "38.1", "38", "38", "37.9", "37.8", "37.7", "37.7", "37.6", "37.5",
        "37.4", "37.4", "37.3", "37.2", "37.2", "37.1", "37", "36.9", "36.9", "36.8",
        "36.7", "36.6", "36.6", "36.5", "36.4", "36.3", "36.3", "36.2", "36.1", "36",
        "36", "35.9", "35.8", "35.7", "35.7", "35.6", "35.5", "35.4", "35.4", "35.3",
        "35.2", "35.2", "35.1", "35", "34.9", "34.9", "34.8", "34.7", "34.6", "34.6",
        "34.5", "34.4", "34.3", "34.3", "34.2", "34.1", "34", "34", "33.9", "33.8",
        "33.7", "33.7", "33.6", "33.5", "33.4", "33.4", "33.3", "33.2", "33.2", "33.1",
        "33", "32.9", "32.9", "32.8", "32.7", "32.6", "32.6", "32.5", "32.4", "32.3",
        "32.3", "32.2", "32.1", "32", "32", "31.9", "31.8", "31.7", "31.7", "31.6",
        "31.5", "31.4", "31.4", "31.3", "31.2", "31.2", "31.1", "31", "30.9", "30.9",
        "30.8", "30.7", "30.6", "30.6", "30.5", "30.4", "30.3", "30.3", "30.2", "30.1",
        "30", "30", "29.9", "29.8", "29.7", "29.7", "29.6", "29.5", "29.4", "29.4",
        "29.3", "29.2", "29.2", "29.1", "29", "28.9", "28.9", "28.8", "28.7", "28.6",
        "28.6", "28.5", "28.4", "28.3", "28.3", "28.2", "28.1", "28", "28", "27.9",
        "27.8", "27.7", "27.7", "27.6", "27.5", "27.4", "27.4", "27.3", "27.2", "27.2",
        "27.1", "27", "26.9", "26.9", "26.8", "26.7", "26.6", "26.6", "26.5", "26.4",
        "26.3", "26.3", "26.2", "26.1", "26", "26", "25.9", "25.8", "25.7", "25.7",
        "25.6", "25.5", "25.4", "25.4", "25.3", "25.2", "25.2", "25.1", "25", "24.9",
        "24.9", "24.8", "24.7", "24.6", "24.6", "24.5", "24.4", "24.3", "24.3", "24.2",
        "24.1", "24", "24", "23.9", "23.8", "23.7", "23.7", "23.6", "23.5", "23.4",
        "23.4", "23.3", "23.2", "23.2", "23.1", "23", "22.9", "22.9", "22.8", "22.7",
        "22.6", "22.6", "22.5", "22.4", "22.3", "22.3", "22.2", "22.1", "22", "22",
        "21.9", "21.8", "21.7", "21.7", "21.6", "21.5", "21.4", "21.4", "21.3", "21.2",
        "21.2", "21.1", "21", "20.9", "20.9", "20.8", "20.7", "20.6", "20.6", "20.5",
        "20.4", "20.3", "20.3", "20.2", "20.1", "20", "20", "19.9", "19.8", "19.7",
        "19.7", "19.6", "19.5", "19.4", "19.4", "19.3", "19.2", "19.2", "19.1", "19",
        "18.9", "18.9", "18.8", "18.7", "18.6", "18.6", "18.5", "18.4", "18.3", "18.3",
        "18.2", "18.1", "18", "18", "17.9", "17.8", "17.7", "17.7", "17.6", "17.5",
        "17.4", "17.4", "17.3", "17.2", "17.2", "17.1", "17", "16.9", "16.9", "16.8",
        "16.7", "16.6", "16.6", "16.5", "16.4", "16.3", "16.3", "16.2", "16.1", "16",
        "16", "15.9", "15.8", "15.7", "15.7", "15.6", "15.5", "15.4", "15.4", "15.3",
        "15.2", "15.2", "15.1", "15", "14.9", "14.9", "14.8", "14.7", "14.6", "14.6",
        "14.5", "14.4", "14.3", "14.3", "14.2", "14.1", "14", "14", "13.9", "13.8",
        "13.7", "13.7", "13.6", "13.5", "13.4", "13.4", "13.3", "13.2", "13.2", "13.1",
        "13", "12.9", "12.9", "12.8", "12.7", "12.6", "12.6", "12.5", "12.4", "12.3",
        "12.3", "12.2", "12.1", "12", "12", "11.9", "11.8", "11.7", "11.7", "11.6",
        "11.5", "11.4", "11.4", "11.3", "11.2", "11.2", "11.1", "11", "10.9", "10.9",
        "10.8", "10.7", "10.6", "10.6", "10.5", "10.4", "10.3", "10.3", "10.2", "10.1",
        "10", "10", "9.9", "9.8", "9.7", "9.7", "9.6", "9.5", "9.4", "9.4", "9.3", "9.2",
        "9.2", "9.1", "9", "8.9", "8.9", "8.8", "8.7", "8.6", "8.6", "8.5", "8.4", "8.3",
        "8.3", "8.2", "8.1", "8", "8", "7.9", "7.8", "7.7", "7.7", "7.6", "7.5", "7.4",
        "7.4", "7.3", "7.2", "7.2", "7.1", "7", "6.9", "6.9", "6.8", "6.7", "6.6", "6.6",
        "6.5", "6.4", "6.3", "6.3", "6.2", "6.1", "6", "6", "5.9", "5.8", "5.7", "5.7",
        "5.6", "5.5", "5.4", "5.4", "5.3", "5.2", "5.2", "5.1", "5", "4.9", "4.9", "4.8",
        "4.7", "4.6", "4.6", "4.5", "4.4", "4.3", "4.3", "4.2", "4.1", "4", "4", "3.9",
        "3.8", "3.7", "3.7", "3.6", "3.5", "3.4", "3.4", "3.3", "3.2", "3.2", "3.1", "3",
        "2.9", "2.9", "2.8", "2.7", "2.6", "2.6", "2.5", "2.4", "2.3", "2.3", "2.2",
        "2.1", "2", "2", "1.9", "1.8", "1.7", "1.7", "1.6", "1.5", "1.4", "1.4", "1.3",
        "1.2", "1.2", "1.1", "1", "0.9", "0.9", "0.8", "0.7", "0.6", "0.6", "0.5", "0.4")
}
