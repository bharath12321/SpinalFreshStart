import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirebaseFirestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.FileInputStream

fun main() {

    val serviceAccountFile = "path/to/serviceAccountKey.json"

    val options = FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setDatabaseUrl("https://neuroflex-2023-default-rtdb.firebaseio.com")
        .build()

    FirebaseApp.initializeApp(options)
    val firestore =  Firestore.getInstance()
    val collectionName = "sessionBend"
    val jsonFilePath = "/Users/aashishkulkarni/Documents/SpinalFreshStart/app/src/main/java/com/example/spinalfreshstart/model_bend_angle.json"
    val jsonFile = FileInputStream(jsonFilePath)
    val jsonData = jsonFile.reader().readText()
    jsonFile.close()
}