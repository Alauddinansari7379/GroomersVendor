package com.groomers.groomersvendor.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityBookingDetailsBinding
import com.squareup.picasso.Picasso

class BookingDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve each extra individually
        val error = intent.getStringExtra("error") ?: ""
        val address = intent.getStringExtra("address") ?: ""
        val comments = intent.getStringExtra("comments") ?: ""
        val createdAt = intent.getStringExtra("created_at") ?: ""
        val customerName = intent.getStringExtra("customerName") ?: ""
        val customerComments = intent.getStringExtra("customer_comments") ?: ""
        val customerId = intent.getIntExtra("customer_id", -1)
        val customerRating = intent.getIntExtra("customer_rating", 0)
        val date = intent.getStringExtra("date") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val endTime = intent.getStringExtra("end_time") ?: ""
        val id = intent.getIntExtra("id", -1)
        val image = intent.getStringExtra("image") ?: ""
        val mobile = intent.getStringExtra("mobile") ?: ""
        val notes = intent.getStringExtra("notes") ?: ""
        val paymentMode = intent.getIntExtra("payment_mode", 0)
        val paymentName = intent.getStringExtra("payment_name") ?: ""
        val price = intent.getIntExtra("price", 0)
        val profilePicture = intent.getStringExtra("profile_picture") ?: ""
        val rating = intent.getIntExtra("rating", 0)
        val serviceName = intent.getStringExtra("serviceName") ?: ""
        val serviceType = intent.getStringExtra("serviceType") ?: ""
        val serviceId = intent.getIntExtra("service_id", 0)
        val slotId = intent.getStringExtra("slot_id") ?: ""
        val slug = intent.getStringExtra("slug") ?: ""
        val startTime = intent.getStringExtra("start_time") ?: ""
        val status = intent.getIntExtra("status", 0)
        val statusForVendor = intent.getStringExtra("status_for_vendor") ?: ""
        val statusName = intent.getStringExtra("status_name") ?: ""
        val time = intent.getStringExtra("time") ?: ""
        val total = intent.getIntExtra("total", 0)
        val updatedAt = intent.getStringExtra("updated_at") ?: ""
        val userType = intent.getStringExtra("user_type") ?: ""
        val vendorId = intent.getIntExtra("vendor_id", 0)

        // Now bind these values to your views
        binding.tvServiceName.text = serviceName
        binding.tvCustomerName.text = customerName
        binding.tvDate.text = date
        binding.tvStartTime.text = startTime
        binding.tvEndTime.text = endTime
        binding.tvAddress.text = address
        binding.tvStatus.text = statusName
        binding.tvDescription.text = description
        binding.tvPrice.text = price.toString()
        binding.tvRating.text = rating.toString()
        binding.tvUserType.text = userType

        // Load profile image if available
        if (profilePicture.isNotEmpty()) {
            Picasso.get()
                .load("https://groomers.co.in/public/uploads/$profilePicture") // replace with your real base URL
                .into(binding.ivProfileImage)
        } else {
            binding.ivProfileImage.setImageResource(R.drawable.error_image)
        }
    }
}
