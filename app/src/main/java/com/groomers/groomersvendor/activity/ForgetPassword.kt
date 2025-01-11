package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityForgetPasswordBinding

class ForgetPassword : AppCompatActivity() {
    private val context=this@ForgetPassword
    private val binding by lazy { ActivityForgetPasswordBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding){
            btnChange.setOnClickListener {
                startActivity(Intent(context,Login::class.java))
            }
            tvRegister.setOnClickListener {
                startActivity(Intent(context,Register::class.java))
            }
        }
    }
}