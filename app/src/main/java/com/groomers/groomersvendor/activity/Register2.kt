package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityRegister2Binding

class Register2 : AppCompatActivity() {
    private val binding by lazy { ActivityRegister2Binding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.card1.setOnClickListener {
            startActivity(Intent(this@Register2, Register4::class.java))
        }
        binding.card2.setOnClickListener {
            startActivity(Intent(this@Register2, Register4::class.java))
        }
        binding.card3.setOnClickListener {
            startActivity(Intent(this@Register2, Register4::class.java))
        }
        binding.card4.setOnClickListener {
            startActivity(Intent(this@Register2, Register4::class.java))
        }
        binding.card5.setOnClickListener {
            startActivity(Intent(this@Register2, Register4::class.java))
        }
        binding.card6.setOnClickListener {
            startActivity(Intent(this@Register2, Register4::class.java))
        }
    }
}