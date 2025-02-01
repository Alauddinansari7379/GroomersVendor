package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.databinding.ActivityAboutBinding

class About : Common() {
    private val binding by lazy { ActivityAboutBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val category = intent.getStringExtra("category")
        // Get the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE
       val businessName = binding.etBusinessName.text.toString()
       val yourName = binding.etYourName.text.toString()
       val phoneNO = binding.etPhoneNo.text.toString()

        // Update the status bar color to match the background color
        updateStatusBarColor(backgroundColor)
        binding.btnContinue3.setOnClickListener {
            val intent = Intent(this, AboutYourCredentials::class.java)
            intent.putExtra("category", category)
            intent.putExtra("businessName", businessName)
            intent.putExtra("yourName", yourName)
            intent.putExtra("phoneNO", phoneNO)
            startActivity(intent)
        }

    }
}