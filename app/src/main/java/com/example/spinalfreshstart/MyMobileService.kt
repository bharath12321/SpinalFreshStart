import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService


class MyMobileService : WearableListenerService() {

    private fun sendUserIdToWear(userId: String) {
        val nodeListTask: Task<List<Node>> = Wearable.getNodeClient(this).connectedNodes
        nodeListTask.addOnSuccessListener { nodes ->
            for (node in nodes) {
                val sendMessageTask: Task<Int> = Wearable.getMessageClient(this)
                    .sendMessage(node.getId(), "/user_id", userId.toByteArray())
            }
        }
    }

    // Override other methods as needed
}
