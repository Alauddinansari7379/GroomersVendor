package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityRegister3Binding
import com.groomers.groomersvendor.databinding.ActivityRegister5Binding

class Register5 : AppCompatActivity() {
    private val binding by lazy { ActivityRegister5Binding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnContinue3.setOnClickListener { startActivity(Intent(this@Register5,Login::class.java)) }

    }
}