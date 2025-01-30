package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.groomers.groomersvendor.R

class AboutYourCredentials : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_about_your_credentials)
        findViewById<Button>(R.id.btnContinue3).setOnClickListener{startActivity(Intent(this@AboutYourCredentials,Register4::class.java))}
    }
}