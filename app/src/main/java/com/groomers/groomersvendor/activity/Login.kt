package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.groomers.groomersvendor.MainActivity
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityLoginBinding
import com.groomers.groomersvendor.helper.AppProgressBar
import com.groomers.groomersvendor.retrofit.ApiServiceProvider
import com.groomers.groomersvendor.viewmodel.LoginViewModel
class Login : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val context = this@Login
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val apiService = ApiServiceProvider.getApiService()

        with(binding) {
            btnLogin.setOnClickListener {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                if (email.isEmpty()) {
                    binding.etEmail.error = "Please enter your email"
                    binding.etEmail.requestFocus()
                    return@setOnClickListener
                }

                if (password.isEmpty()) {
                    binding.etPassword.error = "Please enter your password"
                    binding.etPassword.requestFocus()
                    return@setOnClickListener
                }

                // If both fields are non-empty, proceed with login
                viewModel.login(apiService, email, password)
            }

            // Observe isLoading to show/hide progress
            viewModel.isLoading.observe(context) { isLoading ->
                if (isLoading) {
                    AppProgressBar.showLoaderDialog(context)
                } else {
                    AppProgressBar.hideLoaderDialog()
                }
            }

            // Observe the result of the login attempt
            viewModel.modelLogin.observe(context) { modelLogin ->
                modelLogin?.let {
                    // If login is successful, navigate to MainActivity
                    startActivity(Intent(context, MainActivity::class.java))
                    finish()
                }
            }

            // Observe error message if login fails
            viewModel.errorMessage.observe(context) { errorMessage ->
                if (errorMessage.isNotEmpty()) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            tvRegister.setOnClickListener {
                startActivity(Intent(context, Register2::class.java))
            }

            tvForget.setOnClickListener {
                startActivity(Intent(context, ForgetPassword::class.java))
            }
        }
    }
}

