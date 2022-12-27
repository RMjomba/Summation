package com.reginaldateya.summation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var tvNewUser : TextView
    private lateinit var tvOldUser : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvNewUser = findViewById(R.id.tvNewUser)
        tvOldUser = findViewById(R.id.tvOldUser)
        tvNewUser.setOnClickListener {
            startActivity(Intent(this@MainActivity, Login::class.java))
            finish()
        }
        tvOldUser.setOnClickListener {
            startActivity(Intent(this@MainActivity, Login::class.java))
        }

    }

}