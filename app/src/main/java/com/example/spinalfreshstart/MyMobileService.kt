package com.example.spinalfreshstart
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService


class MyMobileService : WearableListenerService() {

    companion object SessionState {
        var state: String = "stop"
    }
    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        Log.d("received","yes")
        if ("timer" == messageEvent.path) {
            state = String(messageEvent.data)
            // Handle user ID: Display user data or show login state.
        }

    }
    // Override other methods as needed
}
