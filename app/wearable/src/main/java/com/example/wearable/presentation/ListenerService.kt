package com.example.wearable.presentation

import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService


class ListenerService : WearableListenerService() {

    object UserData {
        var userId: String? = null
        var liveAngle: String? = null
    }
    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        if ("/user_id" == messageEvent.path) {
            UserData.userId = String(messageEvent.data)
            // Handle user ID: Display user data or show login state.
        }
        if("/liveAngle" == messageEvent.path){
            UserData.liveAngle = String(messageEvent.data)
        }
    }
    // Override other methods as needed
}
