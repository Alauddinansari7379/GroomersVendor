package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.groomers.groomersvendor.MainActivity
import com.groomers.groomersvendor.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val context=this@Login
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            btnLogin.setOnClickListener {
                startActivity(Intent(context,MainActivity::class.java))
            }

            tvRegister.setOnClickListener {
                startActivity(Intent(context,Register2::class.java))
            }

            tvForget.setOnClickListener {
                startActivity(Intent(context,ForgetPassword::class.java))
            }
        }

    }
}
