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
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
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

@Composable
fun RunScreen(navController: NavController, context: Context) {
    var timerValue = remember{ mutableStateOf(0) }
    var angleValue = remember { mutableStateOf(20.0f)}
    var vibrate = remember { mutableStateOf(false) }
    var wearSend: WearSender = WearSender(context)
    var prevState: Int = 0//state 0 = init

    var timerRunningState by remember { mutableStateOf(TimerState.STOPPED) }
    var backgroundColor by remember { mutableStateOf(Color.Green) }
    var manageTimer by remember {mutableStateOf(false)}
    var phoneSession by remember {mutableStateOf(false)}
    var wearSession by remember {mutableStateOf(false)}
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun playTimer(time: Int, phone: Boolean){

        if(timerRunningState == TimerState.STOPPED){
            if(phone){
                timerRunningState = TimerState.RUNNING
                timerValue.value = time
            }else{
                timerRunningState = TimerState.RUNNING
                manageTimer = !manageTimer
                timerValue.value = time
            }

        }

    }
    fun stopTimer(phone: Boolean){
        if(phone){
            timerRunningState = TimerState.STOPPED
        }else{
            timerRunningState = TimerState.STOPPED
            manageTimer = !manageTimer
        }


    }
    //Coroutine functions
    LaunchedEffect(key1 = true){
        while(true){
            if (timerRunningState == TimerState.RUNNING) {
                angleValue.value = ListenerService.liveAngle
            }
            /*if(angleNum < Angles.nums.size - 1) {
                angleNum += 1
            }else{
                angleNum = 0
            }*/
            delay(50L)
        }
    }
    //handle session start/stop between devices.
    LaunchedEffect(key1 =true){
        while(true){
            phoneSession = ListenerService.phoneSession

            if(phoneSession && wearSession){//state 1 both on
                if(prevState == 2){
                    playTimer(ListenerService.phoneTimer, true)
                }else if(prevState == 3){
                    playTimer(ListenerService.phoneTimer, false)
                }
                prevState = 1
            }else if(phoneSession && !wearSession){//state 2 phone on wear off set wtch timer to phone timer, and set wearSession to true
                if(prevState == 1){//turned wear off
                    stopTimer(false)
                    wearSend.sendMessage("/session",wearSession.toString().toByteArray())
                }else if(prevState == 4){//turned phone on
                    wearSession = true
                }else if(prevState == 0){//started wear with phone on
                    wearSession = true
                }
                prevState = 2
            }else if(!phoneSession && wearSession){//state 3 wear on phone off send phone start message, wait for phone, then set on.
                if(prevState == 4){//wear switched on from both off
                    wearSend.sendMessage("/session",wearSession.toString().toByteArray())
                }else if(prevState == 1){//phone switched off from both on
                    stopTimer(true)
                    wearSession = false
                }
                prevState = 3
            }else if(!phoneSession && !wearSession){//state 4 both off set timer off
                if(prevState ==0){//both off from init
                    //everything should already be off
                }else if(prevState == 2){//both off from phone on wear off
                    //everything off
                }else if(prevState == 3){//both off from phone off wear on
                    //everything off
                }
                prevState = 4
            }
            delay(100L)
        }
    }
    // Simulating color change based on angle value
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
    //View components
    SpinalFreshStartWearTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(10.dp)
        ) {
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
                if (manageTimer){
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {
                        Row(verticalAlignment = Alignment.CenterVertically ,horizontalArrangement = Arrangement.Center) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                                contentDescription = null,  // Provide appropriate content description
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable(enabled = true, onClick = { wearSession = true }),
                                contentScale = ContentScale.Fit
                            )

                            Image(
                                painter = painterResource(id = R.drawable.stop),
                                contentDescription = null,  // Provide appropriate content description
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable(enabled = true, onClick = { wearSession = false }),
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
//staticish functions
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
    RUNNING, STOPPED
}
