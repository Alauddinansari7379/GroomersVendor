package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.databinding.ActivityAboutBinding
import com.groomers.groomersvendor.viewmodel.MyApplication

class About : Common() {
    private val binding by lazy { ActivityAboutBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        (application as MyApplication).registerViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Get the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE

        // Update the status bar color to match the background color
        updateStatusBarColor(backgroundColor)
        binding.btnContinue3.setOnClickListener {
            val businessName = binding.etBusinessName.text.toString()
            val yourName = binding.etYourName.text.toString()
            val phoneNO = binding.etPhoneNo.text.toString()
            val aboutYourBusiness = binding.etAboutBusiness.text.toString()
            viewModel.businessName = businessName
            viewModel.yourName = yourName
            viewModel.mobile = phoneNO
            viewModel.aboutBusiness = aboutYourBusiness

            if (businessName.isEmpty()) {
                binding.etBusinessName.error = "Please enter your business name"
                binding.etBusinessName.requestFocus()
                return@setOnClickListener
            }
            if (yourName.isEmpty()) {
                binding.etYourName.error = "Please enter your name"
                binding.etYourName.requestFocus()
                return@setOnClickListener
            }
            if (phoneNO.isEmpty()) {
                binding.etPhoneNo.error = "Please enter your phone no"
                binding.etPhoneNo.requestFocus()
                return@setOnClickListener
            } else if (phoneNO.length < 10) {
                binding.etPhoneNo.error = "Please enter valid phone no"
                binding.etPhoneNo.requestFocus()
                return@setOnClickListener
            }else if(aboutYourBusiness.isEmpty()){
                binding.etAboutBusiness.error = "Please select about your business"
            }
            val intent = Intent(this, AboutYourCredentials::class.java)
            startActivity(intent)
        }

    }
}