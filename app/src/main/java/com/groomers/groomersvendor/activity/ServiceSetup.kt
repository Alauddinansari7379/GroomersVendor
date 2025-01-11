package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.groomers.groomersvendor.MainActivity
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityServiceSetupBinding

class ServiceSetup : AppCompatActivity() {
    private val binding by lazy { ActivityServiceSetupBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnContinue3.setOnClickListener { startActivity(Intent(this@ServiceSetup,RegisterSuccess::class.java)) }
        binding.card1.setOnClickListener{startActivity(Intent(this@ServiceSetup,RegisterSuccess::class.java))}

    }
}