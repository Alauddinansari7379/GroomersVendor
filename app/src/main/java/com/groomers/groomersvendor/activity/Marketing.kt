package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.databinding.ActivityMarketingBinding

class Marketing : Common() {
    private val binding by lazy { ActivityMarketingBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE

        // Update the status bar color to match the background color
        updateStatusBarColor(backgroundColor)

        binding.root.setOnClickListener {
            startActivity(
                Intent(
                    this@Marketing,
                    UploadCover::class.java
                )
            )
        }
    }
}