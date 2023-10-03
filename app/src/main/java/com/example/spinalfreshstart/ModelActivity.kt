package com.example.spinalfreshstart

import android.app.Activity
import android.opengl.Matrix
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Choreographer
import android.view.SurfaceView
import android.widget.Button
import android.widget.TextView
import com.google.android.filament.utils.KTX1Loader
import com.google.android.filament.utils.ModelViewer
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.initialize
import java.nio.ByteBuffer
import java.util.concurrent.Executors
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseError
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit


class ModelActivity : Activity() {

    companion object {
        init {
            com.google.android.filament.utils.Utils.init()
        }
    }

    private lateinit var surfaceView: SurfaceView
    private lateinit var choreographer: Choreographer
    private lateinit var modelViewer: ModelViewer


    private var isSessionActive = false
    private var sessionElapsedTime: Long = 0

    // Constants for controlling animation speed
    private val animationSpeed = 20.0 // Adjust this value to control animation speed

    // Define your animation parameters (sampleAngles and sampleAngularVelocities) here
    val sampleAngles = arrayOf(
        10.3f, 9.7f, 9.0f, 8.3f, 7.6f, 7.0f, 6.4f, 5.9f, 5.6f, 5.1f,
        4.4f, 3.6f, 2.9f, 2.4f, 2.2f, 2.1f, 2.0f, 1.9f, 2.2f, 3.0f,
        4.4f, 5.8f, 7.1f, 8.3f, 9.4f, 10.3f, 0.0f, 15.3f, 16.3f, 17.0f,
        17.5f, 18.5f, 19.1f, 20.3f, 21.4f, 22.4f, 23.6f, 24.7f, 26.1f,
        27.4f, 28.8f, 30.1f, 31.5f, 33.1f, 34.5f, 35.7f, 37.1f, 38.4f,
        39.8f, 41.2f, 42.8f, 44.4f, 45.9f, 47.4f, 48.8f, 50.3f, 51.9f,
        53.4f, 54.6f, 55.8f, 56.8f, 57.8f, 58.7f, 59.5f, 60.2f, 60.8f,
        61.2f, 61.7f, 62.0f, 62.2f, 62.5f, 62.9f, 63.0f, 62.8f, 62.6f,
        62.3f, 61.9f, 61.3f, 60.4f, 59.1f, 57.6f, 55.5f, 53.5f, 51.4f,
        49.0f, 46.6f, 44.0f, 41.4f, 38.8f, 36.3f, 34.0f, 32.0f, 29.9f,
        27.9f, 26.0f, 24.1f, 22.1f, 19.9f, 17.7f, 15.5f, 13.3f, 10.9f,
        8.7f, 6.4f, 4.4f, 2.5f, 1.3f, 2.0f, 3.8f, 5.5f, 7.2f, 8.8f,
        10.5f, 12.1f, 13.8f, 15.2f, 16.4f, 17.3f, 18.0f, 18.4f, 18.3f,
        18.4f, 18.4f, 18.5f, 18.5f, 18.5f
    )

    val sampleAngularVelocities = arrayOf(
        0.0f, 14.3f, 14.8f, 13.5f, 11.7f, 10.4f, 8.3f, 8.9f, 12.2f, 15.8f,
        17.5f, 16.6f, 14.0f, 8.5f, 3.9f, 9.8f, 19.1f, 23.2f, 28.1f, 30.4f,
        28.3f, 26.7f, 24.5f, 20.7f, 136.7f, 7.2f, 163.2f, 16.9f, 12.3f, 15.2f,
        16.2f, 17.8f, 22.3f, 21.8f, 22.2f, 23.4f, 25.5f, 26.7f, 27.8f, 27.7f,
        27.8f, 30.6f, 30.3f, 26.5f, 26.1f, 27.5f, 28.1f, 29.6f, 31.1f, 31.5f,
        30.7f, 30.4f, 29.6f, 29.3f, 31.4f, 31.0f, 27.0f, 24.7f, 22.2f, 20.2f,
        18.5f, 16.6f, 15.8f, 13.1f, 10.3f, 8.8f, 7.8f, 5.6f, 5.4f, 7.2f,
        4.8f, 1.9f, 4.6f, 4.8f, 6.7f, 9.9f, 15.3f, 22.2f, 28.4f, 36.5f,
        41.1f, 40.8f, 45.0f, 49.0f, 50.4f, 52.0f, 51.9f, 51.1f, 48.6f, 43.9f,
        41.6f, 41.1f, 39.1f, 38.5f, 39.4f, 41.7f, 44.0f, 44.5f, 44.6f, 45.7f,
        46.5f, 45.7f, 43.7f, 41.7f, 39.8f, 39.8f, 41.0f, 38.0f, 34.3f, 33.1f,
        33.0f, 33.0f, 33.7f, 31.1f, 26.4f, 21.3f, 16.2f, 11.0f, 3.4f, 1.1f,
        0.9f, 1.0f, 1.4f, 0.7f, 1.7f, 2.6f
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_model)

        findViewById<TextView>(R.id.titleTextView)
        surfaceView = findViewById(R.id.modelSurfaceView)

        val backButton = findViewById<Button>(R.id.backButton)
        val startSessionButton = findViewById<Button>(R.id.startSessionButton)
        val endSessionButton = findViewById<Button>(R.id.endSessionButton)
        val sessionTimer = findViewById<TextView>(R.id.sessionStatusTextView)

        FirebaseApp.initializeApp(this)
        connectFirebase()

        backButton.setOnClickListener {
            finish()
        }

        choreographer = Choreographer.getInstance()
        modelViewer = ModelViewer(surfaceView)
        surfaceView.setOnTouchListener(modelViewer)

        loadModelAndEnvironment("scene", "venetian_crossroads_2k")

        startSessionButton.setOnClickListener {

            isSessionActive = true
            sessionElapsedTime = 0
            sessionTimer.setTextColor(getColor(android.R.color.holo_green_light))
            sessionTimer.text = "Current Session: 00:00"

            connectFirebase()

            val sessionHandler = Handler(Looper.getMainLooper())
            sessionHandler.post(object : Runnable {
                override fun run() {
                    if(isSessionActive == true) {
                        sessionElapsedTime++

                        val minutes = TimeUnit.SECONDS.toMinutes(sessionElapsedTime)
                        val seconds = TimeUnit.SECONDS.toSeconds(sessionElapsedTime) - TimeUnit.MINUTES.toSeconds(minutes)

                        sessionTimer.text = String.format("Current Session: %02d:%02d", minutes, seconds)

                        sessionHandler.postDelayed(this, 1000)
                    }
                }
            })
        }

        endSessionButton.setOnClickListener {
            isSessionActive = false
            sessionTimer.setTextColor(getColor(android.R.color.holo_red_light))
        }

    }

    private fun connectFirebase() {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("bendAngleData")
        println("Connected with Firebase...")

        val timer = Timer()
        var counter = 0

        timer.schedule(object: TimerTask(){
            override fun run() {
                if(counter < sampleAngles.size && isSessionActive == true) {
                    myRef.setValue(sampleAngles[counter])
                    counter++
                } else {
                    timer.cancel()
                }
            }
        }, 0, 100)
    }



    private val executor = Executors.newSingleThreadExecutor()

    private val frameCallback = object : Choreographer.FrameCallback {
        private var startTime = System.nanoTime()
        private val handler = Handler(Looper.getMainLooper())

        override fun doFrame(currentTime: Long) {
            choreographer.postFrameCallback(this)

            executor.execute {
                try {
                    var elapsedTime = (currentTime - startTime).toDouble() / 1_000_000_000

                    handler.post {
                        modelViewer.render(currentTime)

                        // Use sampleAngles and sampleAngularVelocities to control the animation
                        val sampleIndex = (elapsedTime * animationSpeed) % sampleAngles.size

                        // Reset startTime if we've reached the end of the animation
                        if (sampleIndex < 1) {
                            startTime = currentTime
                            elapsedTime = 0.0
                        }

                        val currentSampleAngle = sampleAngles[sampleIndex.toInt()]
                        val currentSampleAngularVelocity = sampleAngularVelocities[sampleIndex.toInt()]

                        val rotationMatrix = FloatArray(16)
                        Matrix.setRotateM(rotationMatrix, 0, currentSampleAngle, 1f, 0f, 0f) // Rotate around the X axis

                        // Only apply rotation matrix to Spine_53 entity
                        modelViewer.asset?.getFirstEntityByName("Spine_53")?.setTransform(rotationMatrix)

                        modelViewer.animator?.updateBoneMatrices()
                        modelViewer.render(currentTime)
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun Int.setTransform(mat: FloatArray) {
        val tm = modelViewer.engine.transformManager
        tm.setTransform(tm.getInstance(this), mat)
    }

    private fun Int.getTransform(): FloatArray {
        val tm = modelViewer.engine.transformManager
        val arr = FloatArray(16)
        tm.getTransform(tm.getInstance(this), arr)
        return arr
    }

    override fun onResume() {
        super.onResume()
        choreographer.postFrameCallback(frameCallback)
    }

    override fun onPause() {
        super.onPause()
        choreographer.removeFrameCallback(frameCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        choreographer.removeFrameCallback(frameCallback)
    }

    private fun loadModelAndEnvironment(modelName: String, ibl: String) {
        loadModelGLTF(modelName)
        loadSkyboxAndIndirectLight(ibl)
    }

    private fun loadModelGLB(name: String) {
        val buffer = readAsset("$name.glb")
        modelViewer.loadModelGlb(buffer)
        modelViewer.transformToUnitCube()
    }

    private fun loadModelGLTF(name: String) {
        val buffer = readAsset("$name.gltf")
        modelViewer.loadModelGltf(buffer) { uri -> readAsset("$uri") }
        modelViewer.transformToUnitCube()
    }

    private fun loadSkyboxAndIndirectLight(ibl: String) {
        val bufferSkybox = readAsset("envs/$ibl/${ibl}_skybox.ktx")
        val bufferIndirectLight = readAsset("envs/$ibl/${ibl}_ibl.ktx")

        val skybox = KTX1Loader.createSkybox(modelViewer.engine, bufferSkybox)
        val indirectLight = KTX1Loader.createIndirectLight(modelViewer.engine, bufferIndirectLight)
        indirectLight.intensity = 10000f

        modelViewer.scene.skybox = skybox
        modelViewer.scene.indirectLight = indirectLight
    }

    private fun readAsset(assetName: String): ByteBuffer {
        val input = assets.open(assetName)
        val bytes = ByteArray(input.available())
        input.read(bytes)
        return ByteBuffer.wrap(bytes)
    }

}
