package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.MainActivity
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.adapter.SliderAdapter
import com.groomers.groomersvendor.databinding.ActivityWelcomeBinding
import com.groomers.groomersvendor.model.SliderItem
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class Welcome : Common() {
    private val binding by lazy { ActivityWelcomeBinding.inflate(layoutInflater) }

    @Inject
    lateinit var sessionManager: SessionManager

    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE
        updateStatusBarColor(backgroundColor)

        val sliderItems = listOf(
            SliderItem("Welcome to Groomers\nFor professionals", R.drawable.welcome_1),
            SliderItem(
                "All in One solution\nto manage bookings, do Marketing and to grow your business.",
                R.drawable.welcome_2
            )
        )

        if (sessionManager.isLogin) {
            startActivity(Intent(this@Welcome, MainActivity::class.java))
            finish()
        }

        val adapter = SliderAdapter(sliderItems, binding.viewPager) {
            val intent = Intent(this, SelectLanguage::class.java)
            startActivity(intent)
            finish()
        }
        binding.viewPager.adapter = adapter

        // Start auto-slide
        startAutoSlide(sliderItems.size)
    }

    private fun startAutoSlide(totalPages: Int) {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (currentPage < totalPages - 1) {
                    currentPage++
                } else {
                    currentPage = 0 // Reset to the first slide if needed
                }
                binding.viewPager.setCurrentItem(currentPage, true)
                handler.postDelayed(this, 5000) // Repeat every 5 seconds
            }
        }, 5000) // Initial delay
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Stop handler when activity is destroyed
    }
}
