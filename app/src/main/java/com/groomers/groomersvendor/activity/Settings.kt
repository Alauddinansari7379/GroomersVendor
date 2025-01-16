package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.databinding.ActivitySettingsBinding

class Settings : Common() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Get the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE

        // Update the status bar color to match the background color
        updateStatusBarColor(backgroundColor)
        binding.mainLayout.setOnClickListener{startActivity(Intent(this@Settings,Checkout::class.java))}

    }
}