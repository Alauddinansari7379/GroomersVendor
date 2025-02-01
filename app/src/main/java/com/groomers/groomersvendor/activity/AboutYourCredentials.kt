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
        val userId = binding.etUserId.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        binding.btnContinue3.setOnClickListener {
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