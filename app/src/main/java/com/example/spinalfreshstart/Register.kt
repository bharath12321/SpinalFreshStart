package com.example.spinalfreshstart

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {
    var editTextUsername: TextInputEditText? = null
    var editTextPassword: TextInputEditText? = null
    var buttonReg: Button? = null
    private var mAuth: FirebaseAuth? = null
    var progressBar: ProgressBar? = null
    var goToLogin: TextView? = null
    public override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        val editTextEmail = findViewById<EditText>(R.id.EmailTextBoxRegister)
        val editTextPassword = findViewById<EditText>(R.id.passwordTextBox)
        buttonReg = findViewById(R.id.registerButton)
        progressBar = findViewById(R.id.progressBarRegister)
        goToLogin = findViewById(R.id.goToLogIn)
        goToLogin?.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        })
        buttonReg?.setOnClickListener(View.OnClickListener {
            progressBar?.visibility = View.VISIBLE
            val email: String = editTextEmail.text.toString()
            val password: String = editTextPassword.text.toString()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this@Register, "Enter username", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this@Register, "Enter password", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    progressBar?.setVisibility(View.GONE)
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(
                            this@Register, "Account Created",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@Register, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        })
    }
}