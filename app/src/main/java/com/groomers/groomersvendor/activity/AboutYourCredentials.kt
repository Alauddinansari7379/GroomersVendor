package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityAboutYourCredentialsBinding

class AboutYourCredentials : AppCompatActivity() {
    private val binding by lazy { ActivityAboutYourCredentialsBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        val category = intent.getStringExtra("category")
        val businessName = intent.getStringExtra("businessName")
        val yourName = intent.getStringExtra("yourName")
        val phoneNO = intent.getStringExtra("phoneNO")

        binding.btnContinue3.setOnClickListener {
            val userId = binding.etUserId.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (userId.isEmpty()) {
                binding.etUserId.error = "Please enter user id"
                binding.etUserId.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.etPassword.error = "Please enter password"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.etEmail.error = "Please enter your email"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }

            val intent = Intent(this, Register4::class.java)
            intent.putExtra("category", category)
            intent.putExtra("businessName", businessName)
            intent.putExtra("yourName", yourName)
            intent.putExtra("phoneNO", phoneNO)
            intent.putExtra("userId", userId)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            startActivity(intent)
        }
    }
}