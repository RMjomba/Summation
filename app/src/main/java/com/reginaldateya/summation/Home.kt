package com.reginaldateya.summation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class Home : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var signOut: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        signOut = findViewById(R.id.signOut)

        signOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, Login::class.java))
        }
    }
}