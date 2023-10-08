package com.example.wearable.presentation

import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import android.util.Log

class ListenerService : WearableListenerService() {

    companion object {
        var userEmail: String = "noname"
        var liveAngle: Float = 20f

    }
    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        Log.d("reciever","a message was recieved")
        if ("/SentFromPhone/email" == messageEvent.path) {
            userEmail = String(messageEvent.data)
            // Handle user ID: Display user data or show login state.
        }else if("/SentFromPhone/angle" == messageEvent.path){
            liveAngle = String(messageEvent.data).toFloat()
        }

    }
    // Override other methods as needed
}
