package com.example.wearable.presentation

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.Node

class WearSender (context : Context){
    private val messageClient: MessageClient = Wearable.getMessageClient(context)
    private val nodeTasks: Task<List<Node>> = Wearable.getNodeClient(context).connectedNodes

    fun sendMessage(path: String, data: ByteArray){
        nodeTasks.addOnSuccessListener { nodes ->
            for(node in nodes){
                messageClient.sendMessage(node.id,path,data)
            }
        }
    }
}