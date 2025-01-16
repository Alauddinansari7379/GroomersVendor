package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.MainActivity
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityServiceSetupBinding

class ServiceSetup : Common() {
    private val binding by lazy { ActivityServiceSetupBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Get the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE

        // Update the status bar color to match the background color
        updateStatusBarColor(backgroundColor)
        binding.btnContinue3.setOnClickListener { startActivity(Intent(this@ServiceSetup,RegisterSuccess::class.java)) }
        binding.card1.setOnClickListener{startActivity(Intent(this@ServiceSetup,RegisterSuccess::class.java))}

    }
}