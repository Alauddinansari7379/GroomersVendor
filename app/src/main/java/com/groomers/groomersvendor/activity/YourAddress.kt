package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.databinding.ActivityYourAddressBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.retrofit.ApiServiceProvider
import com.groomers.groomersvendor.viewmodel.LocationViewModel
import com.groomers.groomersvendor.viewmodel.MyApplication
import com.groomers.groomersvendor.viewmodel.RegisterViewModel

class YourAddress : Common() {
    private val binding by lazy { ActivityYourAddressBinding.inflate(layoutInflater) }
//    private val viewModel: RegisterViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()
    private val viewModel by lazy {
        (application as MyApplication).registerViewModel
    }

    private val context = this@YourAddress
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE
        updateStatusBarColor(backgroundColor)
        val apiService = ApiServiceProvider.getApiService()
        locationViewModel.getCity(apiService)
        // Observe isLoading to show/hide progress
        locationViewModel.isLoading.observe(context) { isLoading ->
            if (isLoading) {
                CustomLoader.showLoaderDialog(context)
            } else {
                CustomLoader.hideLoaderDialog()
            }
        }
        // Observe error message if login fails
        locationViewModel.errorMessage.observe(context) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnContinue.setOnClickListener {
            val selectedCity = binding.spinnerCity.selectedItem.toString()
            val selectedZip = binding.spinnerZip.selectedItem.toString()
            val address1 = binding.etAddress1.text.toString()
            val address2 = binding.etAddress2.text.toString()
            val mapUrl = binding.etMapUrl.text.toString()
            viewModel.city = selectedCity
            viewModel.zipcode = selectedZip
            viewModel.address1 = address1
            viewModel.address2 = address2
            viewModel.mapUrl = mapUrl

            if (address1.isEmpty()) {
                binding.etAddress1.error = "Please enter address 1"
                binding.etAddress1.requestFocus()
                return@setOnClickListener
            }
            if (address2.isEmpty()) {
                binding.etAddress2.error = "Please enter address 2"
                binding.etAddress2.requestFocus()
                return@setOnClickListener
            }
            if (mapUrl.isEmpty()) {
                binding.etMapUrl.error = "Please enter map url"
                binding.etMapUrl.requestFocus()
                return@setOnClickListener
            }

            if (selectedCity == "Select City") {
                Toast.makeText(this, "Please select a city", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedZip == "Select ZIP Code") {
                Toast.makeText(this, "Please select a ZIP Code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this@YourAddress, UploadOwnerIdentity::class.java)
            startActivity(intent)
        }
        setupSpinners()
    }

    private fun setupSpinners() {
        locationViewModel.modelCity.observe(context) { cityResponse ->
            cityResponse?.result?.let { cityList ->
                val cities = listOf("Select City") + cityList.map { it.city }
                val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCity.adapter = cityAdapter
            }
        }


// Populate ZIP code spinner
        val zipCodeList = listOf("Select ZIP Code", "746477", "376", "366")
        val zipCodeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, zipCodeList)
        zipCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerZip.adapter = zipCodeAdapter
    }
}