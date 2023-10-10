package com.example.wearable.presentation

import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import android.util.Log

class ListenerService : WearableListenerService() {

    companion object {
        var userEmail: String = "Not Connected"
        var liveAngle: Float = 20.0f
        var phoneSession: Boolean = false
        var phoneTimer: Int = 0
        var harmAngle: Float = 100.0f

    }
    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        Log.d("reciever","a message was recieved")
        if ("/SentFromPhone/email" == messageEvent.path) {
            if(String(messageEvent.data) == "Successful Login") {
                userEmail = "Connected"
            }
            // Handle user ID: Display user data or show login state.
        }else if("/SentFromPhone/angle" == messageEvent.path){
            liveAngle = String(messageEvent.data).toFloat()
        }else if("/SentFromPhone/session" == messageEvent.path){
            phoneSession = String(messageEvent.data).toBoolean()
        }
        else if("/SentFromPhone/timer" == messageEvent.path){
            phoneTimer = String(messageEvent.data).toLong().toInt()
        }else if("/SentFromPhone/harmAngle" == messageEvent.path){
            harmAngle = String(messageEvent.data).toFloat()
        }

    }
    // Override other methods as needed
}
