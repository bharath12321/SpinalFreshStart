package com.example.spinalfreshstart

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import java.util.Scanner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MobileSender(context: Context): Thread() {
    private val messageClient: MessageClient = Wearable.getMessageClient(context)
    private val nodeTasks: Task<List<Node>> = Wearable.getNodeClient(context).connectedNodes
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    object SessionState {
        var state: String = "stop"
    }

    fun sendMessage(path: String, data: ByteArray) {
        executorService.submit{
            try {
                val nodes = Tasks.await<List<Node>>(nodeTasks)
                if(nodes.isEmpty()){
                    Log.d("nodesSize","empty list of nodes (notconnected)")
                }else{
                    for (node in nodes) {
                        val sendMessageTask = messageClient.sendMessage(node.id, path, data)
                        try {
                            val result = Tasks.await(sendMessageTask)
                            Log.d("node = ",node.displayName)
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            Log.d("result", "failed send")
                        }
                    }
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.d("result", "failed send")
            }
        }
    }
}