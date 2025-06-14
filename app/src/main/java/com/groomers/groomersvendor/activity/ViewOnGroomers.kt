package com.groomers.groomersvendor.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.groomers.groomersvendor.databinding.ActivityBookingDetailBinding
import com.groomers.groomersvendor.retrofit.ApiServiceProvider
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ViewOnGroomers : AppCompatActivity() {

    private var vendorId: String? = null
    private var serviceId: String? = null
    private lateinit var binding: ActivityBookingDetailBinding
     @Inject
    lateinit var sessionManager: SessionManager

    private val imageUrls = mutableListOf<String>()
    private val sliderHandler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this@ViewOnGroomers)
            .load(ApiServiceProvider.IMAGE_URL + sessionManager.coverPictureUrl)
            .placeholder(Color.GRAY.toDrawable())
            .into(binding.imageSlider)

        binding.shopTitle.text = sessionManager.userName
        binding.shopAddress.text = sessionManager.address
        sessionManager.accessToken?.let { token ->
            lifecycleScope.launch {
//                viewModel.getServiceList(token, sessionManager.userType.toString())
//                viewModel.getServiceList(token, "Female")
            }
        } ?: run {
            Toast.makeText(this, "Error: Missing Token", Toast.LENGTH_LONG).show()
        }

        }
    }
