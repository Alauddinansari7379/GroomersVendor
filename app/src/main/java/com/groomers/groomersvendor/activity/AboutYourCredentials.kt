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
            val userId = binding.etUserId.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Validate user ID
            if (userId.isEmpty()) {
                binding.etUserId.error = "Please enter user ID"
                binding.etUserId.requestFocus()
                return@setOnClickListener
            }

            // Validate password
            if (password.isEmpty()) {
                binding.etPassword.error = "Please enter password"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            } else if (password.length < 8) {
                binding.etPassword.error = "Password must be at least 8 characters long"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            // Validate email
            if (email.isEmpty()) {
                binding.etEmail.error = "Please enter your email"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            } else if (!email.contains("gmail.com")) {
                binding.etEmail.error = "Please enter a valid email address"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }

            // Save validated data in ViewModel
            viewModel.userId = userId
            viewModel.email = email
            viewModel.password = password
            viewModel.passwordConfirmation = password

            // Navigate to next screen
            val intent = Intent(this, Register4::class.java)
            startActivity(intent)
        }
    }
}
