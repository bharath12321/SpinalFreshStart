package com.example.spinalfreshstart

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

//features branch
class Login : AppCompatActivity() {
    var editTextUsername: TextInputEditText? = null
    var editTextPassword: TextInputEditText? = null
    var buttonLogin: Button? = null
    private var mAuth: FirebaseAuth? = null
    var progressBar: ProgressBar? = null
    var goToRegister: Button? = null
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
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        val editTextEmail = findViewById<EditText>(R.id.EmailTextBoxLogin)
        val editTextPassword = findViewById<EditText>(R.id.passwordTextBoxLogin)
        buttonLogin = findViewById(R.id.signInButton)
        progressBar = findViewById(R.id.progressBarLogin)
        goToRegister = findViewById(R.id.goToRegisterPage)
        goToRegister?.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, Register::class.java)
            startActivity(intent)
            finish()
        })
        buttonLogin?.setOnClickListener(View.OnClickListener {
            progressBar?.visibility = View.VISIBLE
            val email: String = editTextEmail.text.toString()
            val password: String = editTextPassword.text.toString()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this@Login, "Enter username", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this@Login, "Enter password", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    progressBar?.visibility = View.GONE
                    if (task.isSuccessful) {
                        //send wear users email
                        mAuth!!.currentUser?.email?.let { it1 -> MobileSender(this).sendMessage("/email", it1.toByteArray()) }

                        Toast.makeText(applicationContext, "Login Successful!", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@Login, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        })
    }
}