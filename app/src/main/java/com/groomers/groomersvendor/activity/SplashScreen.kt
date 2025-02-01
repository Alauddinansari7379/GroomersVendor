package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.databinding.ActivitySplashScreenBinding

class SplashScreen : Common() {
    private lateinit var binding: ActivitySplashScreenBinding
    private var context = this@SplashScreen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Get the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE

        // Update the status bar color to match the background color
        updateStatusBarColor(backgroundColor)

        Handler().postDelayed({
            startActivity(Intent(this, Welcome::class.java))
            finish()
        }, 2000)
    }
}