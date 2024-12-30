package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val context=this@Login
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            btnLogin.setOnClickListener {
                startActivity(Intent(context,SubUserLogin::class.java))
            }

            tvRegister.setOnClickListener {
                startActivity(Intent(context,Register::class.java))
            }

            tvForget.setOnClickListener {
                startActivity(Intent(context,ForgetPassword::class.java))
            }
        }

    }
}
