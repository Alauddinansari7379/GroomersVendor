package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.adapter.SliderAdapter
import com.groomers.groomersvendor.databinding.ActivityWelcomeBinding
import com.groomers.groomersvendor.model.SliderItem

class Welcome : Common() {
    private val binding by lazy { ActivityWelcomeBinding.inflate(layoutInflater) }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE

        // Update the status bar color to match the background color
        updateStatusBarColor(backgroundColor)
//        binding.btnGetStarted.setOnClickListener {
//            startActivity(Intent(this@Welcome,Welcome2::class.java))
//        }

        val sliderItems = listOf(
            SliderItem("Welcome to Groomers\nFor professionals", R.drawable.welcome_1),
            SliderItem(
                "All in One solution\nto manage bookings, do Marketing and to grow your business.",
                R.drawable.welcome_2
            )
        )

        binding.viewPager.adapter = SliderAdapter(sliderItems, binding.viewPager) {
            val intent = Intent(this, SelectLanguage::class.java)
            startActivity(intent)
            finish()
        }
    }

}


