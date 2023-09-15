package com.example.modelrenderinga4

import android.app.Activity
import android.opengl.Matrix
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Choreographer
import android.view.SurfaceView
import android.widget.Button
import android.widget.TextView
import com.google.android.filament.utils.KTX1Loader
import com.google.android.filament.utils.ModelViewer
import com.google.android.filament.utils.Utils
import java.nio.ByteBuffer
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

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

    data class CustomQuaternion(val x: Double, val y: Double, val z: Double, val w: Double)
    private val executor = Executors.newSingleThreadExecutor()

    private val frameCallback = object : Choreographer.FrameCallback {
        private val startTime = System.nanoTime()
        private var frameIndex = 0
        private val handler = Handler(Looper.getMainLooper())
        private val frameDelayMillis = 1000 // Delay in milliseconds (adjust as needed)


        // Sample custom quaternion data (replace this with your array of custom quaternions)
        private val sampleQuaternions = arrayOf(
            CustomQuaternion(0.992742288, -0.090424207, 0.065724703, -0.044344961),
            CustomQuaternion(0.992565406, -0.081224479, 0.076890119, -0.048004263),
            CustomQuaternion(0.992295754, -0.067957596, 0.087414215, -0.055584677),
            CustomQuaternion(0.991707247, -0.053559484, 0.097668562, -0.064101248),
            CustomQuaternion(0.990511305, -0.035485333, 0.110962221, -0.072907685),
            CustomQuaternion(0.985822128, -0.015337189, 0.137674497, -0.094684931),
            CustomQuaternion(0.98273102, 0.002082606, 0.15111206, -0.10677336),
            CustomQuaternion(0.982900616, 0.022735224, 0.148414793, -0.106595204),
            CustomQuaternion(0.979459008, 0.037371687, 0.158875277, -0.118414761),
            CustomQuaternion(0.971703296, 0.037563098, 0.189460636, -0.135964652),
            CustomQuaternion(0.971557646, 0.057530371, 0.183449586, -0.138247044),
            CustomQuaternion(0.962870024, 0.056631909, 0.211046773, -0.158535183),
            CustomQuaternion(0.957061899, 0.069416469, 0.220565512, -0.174827717),
            CustomQuaternion(0.955353262, 0.087785494, 0.216419764, -0.180987122),
            CustomQuaternion(0.949089216, 0.097442257, 0.226621482, -0.195901433),
            CustomQuaternion(0.942385559, 0.107791227, 0.235416299, -0.211824633),
            CustomQuaternion(0.936330002, 0.116207136, 0.242773793, -0.225483735),
            CustomQuaternion(0.935039062, 0.131886152, 0.238063309, -0.227230844),
            CustomQuaternion(0.930414645, 0.13671143, 0.242916587, -0.237970805),
            CustomQuaternion(0.930295285, 0.146903654, 0.237310621, -0.23802031),
            CustomQuaternion(0.919773759, 0.108752022, 0.270546418, -0.262666832)
            // Add more custom quaternions as needed
        )

        override fun doFrame(currentTime: Long) {
            choreographer.postFrameCallback(this)

            executor.execute {
                // ... (rest of your code inside doFrame)
                try {
                    TimeUnit.MILLISECONDS.sleep(frameDelayMillis.toLong())

                    // Perform bone transformation and updates on the main thread
                    handler.post {
                        modelViewer.render(currentTime)
                        // ... (rest of your code inside doFrame)

                        val seconds = (currentTime - startTime).toDouble() / 1_000_000_000
                        choreographer.postFrameCallback(this)

                        Executors.newSingleThreadExecutor().execute {
                            try {
                                TimeUnit.MILLISECONDS.sleep(frameDelayMillis.toLong())

                                // Perform bone transformation and updates on the main thread
                                handler.post {
                                    // Selects the relevant bone
                                    modelViewer.asset?.getFirstEntityByName("spine01")?.let { entity ->
                                        // Gets the bone's current transformation matrix
                                        val currentMatrix = FloatArray(16)
                                        entity.getTransform().copyInto(currentMatrix)


                                        val eulerAngles = calculateEulerAngles(sampleQuaternions)
                                        val angleTheta = eulerAngles.second // θ

                                        // Check if θ is over 80 degrees
                                        if (Math.toDegrees(angleTheta.toDouble()) > 80) {
                                            // Display a notification
                                            Log.d("Animation", "Model at an angle over 80 degrees-==-=-=-=-=-=-")
//                    Toast.makeText(
//                        this,
//                        "Model at an angle over 80 degrees",
//                        Toast.LENGTH_SHORT
//                    ).show()

                                            // You can replace Log.d with a notification display mechanism of your choice
                                        }

                                        // Get the current sample custom quaternion
                                        val currentSampleQuaternion = sampleQuaternions[frameIndex % sampleQuaternions.size]

                                        // Increment the frame index to move to the next sample custom quaternion
                                        frameIndex++

                                        // Reset the frame index if it exceeds the number of samples
                                        if (frameIndex >= sampleQuaternions.size) {
                                            frameIndex = 0
                                        }

                                        // Create a rotation matrix from the custom quaternion
                                        val quaternion = currentSampleQuaternion
                                        val rotationMatrix = FloatArray(16)

                                        val x = quaternion.x.toFloat()
                                        val y = quaternion.y.toFloat()
                                        val z = quaternion.z.toFloat()
                                        val w = quaternion.w.toFloat()

                                        val xx = x * x
                                        val xy = x * y
                                        val xz = x * z
                                        val xw = x * w

                                        val yy = y * y
                                        val yz = y * z
                                        val yw = y * w

                                        val zz = z * z
                                        val zw = z * w

                                        rotationMatrix[0] = 1.0f - 2.0f * (yy + zz)
                                        rotationMatrix[1] = 2.0f * (xy - zw)
                                        rotationMatrix[2] = 2.0f * (xz + yw)
                                        rotationMatrix[3] = 0.0f

                                        rotationMatrix[4] = 2.0f * (xy + zw)
                                        rotationMatrix[5] = 1.0f - 2.0f * (xx + zz)
                                        rotationMatrix[6] = 2.0f * (yz - xw)
                                        rotationMatrix[7] = 0.0f

                                        rotationMatrix[8] = 2.0f * (xz - yw)
                                        rotationMatrix[9] = 2.0f * (yz + xw)
                                        rotationMatrix[10] = 1.0f - 2.0f * (xx + yy)
                                        rotationMatrix[11] = 0.0f

                                        rotationMatrix[12] = 0.0f
                                        rotationMatrix[13] = 0.0f
                                        rotationMatrix[14] = 0.0f
                                        rotationMatrix[15] = 1.0f

                                        // Apply the rotation matrix to the current transformation
                                        val tempMatrix = FloatArray(16)
                                        Matrix.multiplyMM(tempMatrix, 0, currentMatrix, 0, rotationMatrix, 0)
                                        tempMatrix.copyInto(currentMatrix)
                                        entity.setTransform(currentMatrix)
                                    }

                                    // Update the rest of the model bones that are dependent on the transformed bone
                                    modelViewer.animator?.updateBoneMatrices()

                                    // Render again after updates
                                    modelViewer.render(currentTime)
                                }
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }
                        }
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }

    }

    // Function to calculate Euler angles from a quaternion
    private fun calculateEulerAngles(quaternionArray: Array<CustomQuaternion>): Triple<Double, Double, Double> {
        // Calculate the average quaternion from the array
        val averageQuaternion = calculateAverageQuaternion(quaternionArray)

        val q0 = averageQuaternion.w
        val q1 = averageQuaternion.x
        val q2 = averageQuaternion.y
        val q3 = averageQuaternion.z

        val phi = Math.atan2(2 * (q0 * q1 + q2 * q3), 1 - 2 * (q1 * q1 + q2 * q2)) // ϕ
        val theta = Math.asin(2 * (q0 * q2 - q3 * q1)) // θ
        val psi = Math.atan2(2 * (q0 * q3 + q1 * q2), 1 - 2 * (q2 * q2 + q3 * q3)) // ψ

        return Triple(phi, theta, psi)
    }

    private fun calculateAverageQuaternion(quaternionArray: Array<CustomQuaternion>): CustomQuaternion {
        // Initialize the sum of quaternions components
        var sumW = 0.0
        var sumX = 0.0
        var sumY = 0.0
        var sumZ = 0.0

        // Sum up the components of all quaternions in the array
        for (quaternion in quaternionArray) {
            sumW += quaternion.w
            sumX += quaternion.x
            sumY += quaternion.y
            sumZ += quaternion.z
        }

        // Calculate the average components
        val averageW = sumW / quaternionArray.size
        val averageX = sumX / quaternionArray.size
        val averageY = sumY / quaternionArray.size
        val averageZ = sumZ / quaternionArray.size

        return CustomQuaternion(averageX, averageY, averageZ, averageW)
    }

    private fun Int.setTransform(mat: FloatArray) {
        val tm = modelViewer.engine.transformManager
        tm.setTransform(tm.getInstance(this), mat)
    }

//    private fun Int.getTransform(): Mat4 {
//        val tm = modelViewer.engine.transformManager
//        val arr = FloatArray(16)
//        tm.getTransform(tm.getInstance(this), arr)
//        return Mat4.of(*arr)
//    }

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

