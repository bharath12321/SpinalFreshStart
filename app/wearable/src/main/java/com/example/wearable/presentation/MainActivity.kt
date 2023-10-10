/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.wearable.presentation
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp(this)
        }
    }
    public fun sendMessage(){

    }

}
@Composable
fun WearApp(context: Context){
    val navController = rememberSwipeDismissableNavController()
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Screen.Landing.route
    ) {
        composable(Screen.Landing.route) {
            LandingView(navController,context)
        }
        composable(Screen.ActivityRun.route) {
            RunScreen(navController,context)
        }
        composable(Screen.Analytics.route){
            AnalyticsScreen(navController)
        }
        composable(Screen.Sessions.route){
            SessionsScreen(navController)
        }
        composable(Screen.TimerManage.route){
            ManageScreen(navController)
        }
    }
}
sealed class Screen(val route: String){
    object Landing : Screen("screen1")
    object ActivityRun : Screen("screen2")
    object Analytics : Screen("screen3")
    object Sessions : Screen("screen4")
    object TimerManage : Screen("screen5")
}