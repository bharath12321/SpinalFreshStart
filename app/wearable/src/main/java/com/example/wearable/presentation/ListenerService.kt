package com.example.wearable.presentation

import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService


class ListenerService : WearableListenerService() {

    object UserData {
        var userEmail: String = "noname"
        var liveAngle: Float? = null

    }
    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        if ("login" == messageEvent.path) {
            UserData.userEmail = String(messageEvent.data)
            // Handle user ID: Display user data or show login state.
        }
        if("angle" == messageEvent.path){
            UserData.liveAngle = String(messageEvent.data).toFloat()
        }

    }
    // Override other methods as needed
}
