package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivitySelectLanguageBinding
import com.groomers.groomersvendor.databinding.ActivityWelcome2Binding

class Welcome2 : AppCompatActivity() {
    private val binding by lazy { ActivityWelcome2Binding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnGetStarted.setOnClickListener {
            startActivity(Intent(this@Welcome2,SelectLanguage::class.java))
        }

    }
}