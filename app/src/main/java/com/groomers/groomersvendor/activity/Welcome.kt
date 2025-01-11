package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityWelcomeBinding

class Welcome : AppCompatActivity() {
    private val binding by lazy { ActivityWelcomeBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnGetStarted.setOnClickListener {
            startActivity(Intent(this@Welcome,Welcome2::class.java))
        }

    }
}