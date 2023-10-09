package com.example.spinalfreshstart

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.wearable.MessageClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    private var mSend: MobileSender? = null
    // TextView textView;
    var user: FirebaseUser? = null
    private var navController: NavController? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSend = MobileSender(this)
        auth = FirebaseAuth.getInstance()
        //textView = findViewById(R.id.user_details_main);
        user = auth!!.currentUser
        if (user == null) {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()

        } else {
            println("Authentication error")
            // textView.setText(user.getEmail());
        }
        MyMobileService.login = true
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottomNavigationView, navController!!)
    }

}