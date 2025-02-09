package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityAboutYourCredentialsBinding
import com.groomers.groomersvendor.viewmodel.MyApplication
import com.groomers.groomersvendor.viewmodel.RegisterViewModel

class AboutYourCredentials : AppCompatActivity() {
    private val binding by lazy { ActivityAboutYourCredentialsBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        (application as MyApplication).registerViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        binding.btnContinue3.setOnClickListener {
            val userId = binding.etUserId.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.userId = userId
            viewModel.email = email
            viewModel.password = password
            viewModel.passwordConfirmation = password
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
            startActivity(intent)
        }
    }
}