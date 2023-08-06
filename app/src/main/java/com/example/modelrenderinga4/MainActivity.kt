package com.example.modelrenderinga4

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startRenderingButton = findViewById<View>(R.id.startRenderingButton)
        startRenderingButton.setOnClickListener {
            // Start the ModelActivity when the button is clicked
            val intent = Intent(this, ModelActivity::class.java)
            startActivity(intent)
        }
    }
}
