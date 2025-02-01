package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.groomers.groomersvendor.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    val context=this@Register
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            btnContinue.setOnClickListener {
                startActivity(Intent(context, Register2::class.java))
            }
            tvLogin.setOnClickListener {
                startActivity(Intent(context, Login::class.java))
            }
        }
    }
}