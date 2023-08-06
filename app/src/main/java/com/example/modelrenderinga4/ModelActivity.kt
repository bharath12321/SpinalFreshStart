package com.example.modelrenderinga4

import android.app.Activity
import android.os.Bundle
import android.view.Choreographer
import android.view.MotionEvent
import android.view.SurfaceView
import android.widget.Button
import android.widget.TextView
import com.google.android.filament.utils.KTXLoader
import com.google.android.filament.utils.ModelViewer
import com.google.android.filament.utils.Utils
import java.nio.ByteBuffer
import kotlin.math.sqrt

class ModelActivity : Activity() {

    companion object {
        init {
            Utils.init()
        }
    }

    private lateinit var surfaceView: SurfaceView
    private lateinit var choreographer: Choreographer
    private lateinit var modelViewer: ModelViewer

    //for camera
    private var previousX = 0f
    private var previousY = 0f
    private var oldDistance = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_model)

        findViewById<TextView>(R.id.titleTextView)
        surfaceView = findViewById(R.id.modelSurfaceView)
        val backButton = findViewById<Button>(R.id.backButton)

        backButton.setOnClickListener {
            // Finish the current activity and go back to the previous activity (MenuActivity)
            finish()
        }

        choreographer = Choreographer.getInstance()
        modelViewer = ModelViewer(surfaceView)
        surfaceView.setOnTouchListener(modelViewer)

        // Load the model, skybox, and indirect light
        loadModelAndEnvironment("human", "venetian_crossroads_2k")
    }


    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(currentTime: Long) {
            choreographer.postFrameCallback(this)
            modelViewer.render(currentTime)
        }
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
        loadModel(modelName)
        loadSkyboxAndIndirectLight(ibl)
    }

    private fun loadModel(name: String) {
        val buffer = readAsset("$name.glb")
        modelViewer.loadModelGlb(buffer)
        modelViewer.transformToUnitCube()
    }

    private fun loadSkyboxAndIndirectLight(ibl: String) {
        val bufferSkybox = readAsset("envs/$ibl/${ibl}_skybox.ktx")
        val bufferIndirectLight = readAsset("envs/$ibl/${ibl}_ibl.ktx")

        val skybox = KTXLoader.createSkybox(modelViewer.engine, bufferSkybox)
        val indirectLight = KTXLoader.createIndirectLight(modelViewer.engine, bufferIndirectLight)
        indirectLight.intensity = 50000f

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

