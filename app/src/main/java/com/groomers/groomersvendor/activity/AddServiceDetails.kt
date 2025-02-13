package com.groomers.groomersvendor.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.viewmodel.MyApplication

class AddServiceDetails : AppCompatActivity() {
    private val viewModel by lazy {
        (application as MyApplication).createServiceViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service_details)
        val date = viewModel.date
        val description = viewModel.description
        val serviceType = viewModel.serviceType
        val images = viewModel.images
        val serviceName = viewModel.serviceName
        val time = viewModel.time
        val category = viewModel.category
        val price = viewModel.price
        val address = viewModel.address
        val slot_time = viewModel.slot_time
    }
}