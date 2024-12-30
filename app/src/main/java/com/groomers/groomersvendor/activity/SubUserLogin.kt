package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.groomers.groomersvendor.MainActivity
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivitySubUserLoginBinding

class SubUserLogin : AppCompatActivity() {
    private val binding by lazy { ActivitySubUserLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            subUser1.setOnClickListener { startActivity(Intent(this@SubUserLogin, MainActivity::class.java))}
            subUser2.setOnClickListener { startActivity(Intent(this@SubUserLogin, MainActivity::class.java))}
            subUser3.setOnClickListener { startActivity(Intent(this@SubUserLogin, MainActivity::class.java))}
            subUser4.setOnClickListener { startActivity(Intent(this@SubUserLogin, MainActivity::class.java))}

        }
    }
}