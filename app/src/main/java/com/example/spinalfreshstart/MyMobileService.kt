package com.example.spinalfreshstart
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService


class MyMobileService : WearableListenerService() {

    companion object SessionState {
        var wearSession: Boolean = false
        var login: Boolean = false
    }
    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        Log.d("received","yes")
        if ("/SentFromWear/session" == messageEvent.path) {
            wearSession = String(messageEvent.data).toBoolean()
            // Handle user ID: Display user data or show login state.
        }else if("/SentFromWear/login" == messageEvent.path){
            if(login){
                MobileSender(this).sendMessage("/email","Successful Login".toByteArray())
            }
        }

    }
    // Override other methods as needed
}
