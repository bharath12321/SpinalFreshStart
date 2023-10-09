package com.example.wearable.presentation

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.Node
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class WearSender (context : Context){
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private val context: Context = context

    fun sendMessage(path: String, data: ByteArray) {

        val messageClient: MessageClient = Wearable.getMessageClient(context)
        val nodeTasks: Task<List<Node>> = Wearable.getNodeClient(context).connectedNodes
        executorService.submit{
            try {
                val nodes = Tasks.await<List<Node>>(nodeTasks)
                if(nodes.isEmpty()){
                    Log.d("nodesSize","empty list of nodes (notconnected)")
                }else{
                    for (node in nodes) {
                        val sendMessageTask = messageClient.sendMessage(node.id,"/SentFromWear$path", data)
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