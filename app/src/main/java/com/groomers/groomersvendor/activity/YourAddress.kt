package com.groomers.groomersvendor.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.databinding.ActivityYourAddressBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.retrofit.ApiServiceProvider
import com.groomers.groomersvendor.viewmodel.LocationViewModel
import com.groomers.groomersvendor.viewmodel.MyApplication
import com.groomers.groomersvendor.viewmodel.RegisterViewModel
import java.io.IOException
import java.util.Locale

class YourAddress : Common() {
    private val binding by lazy { ActivityYourAddressBinding.inflate(layoutInflater) }
//    private val viewModel: RegisterViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()
    private val viewModel by lazy {
        (application as MyApplication).registerViewModel
    }
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val REQUEST_CODE = 100
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
        askPermission()
        // Observe error message if login fails
        locationViewModel.errorMessage.observe(context) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        with(binding){
            imgLocation.setOnClickListener {
                getLastLocation()
            }
        }
        getLastLocation()
        binding.btnContinue.setOnClickListener {
            val selectedCity = binding.edtCity.text.toString()
            val selectedZip = binding.edtZipCode.text.toString()
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
    @SuppressLint("SetTextI18n", "LogNotTimber")
    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient!!.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        try {
                            val geocoder = Geocoder(context, Locale.getDefault())
                            val addresses =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            Log.e(ContentValues.TAG, "addresses[0].latitude: ${addresses?.get(0)?.latitude}")
                            Log.e(ContentValues.TAG, "addresses[0].longitude: ${addresses?.get(0)?.longitude}")

                            val address = addresses?.get(0)
                            val locality = address?.locality
                            val countryName = address?.countryName
                            val countryCode = address?.countryCode
                            val postalCode = address?.postalCode.toString()
                            val subLocality = address?.subLocality
                            val subAdminArea = address?.subAdminArea ?: "Unknown SubAdminArea"
                            val fullAddress = address?.getAddressLine(0)
                            postalCodeNew = postalCode

                            // Update location text
                            Log.e(ContentValues.TAG, "address: $address")
                            Log.e(ContentValues.TAG, "locality: $locality")
                            Log.e(ContentValues.TAG, "countryName: $countryName")
                            Log.e(ContentValues.TAG, "countryCode: $countryCode")
                            Log.e(ContentValues.TAG, "postalCode: $postalCode")
                            Log.e(ContentValues.TAG, "subLocality: $subLocality")
                            Log.e(ContentValues.TAG, "subAdminArea: $subAdminArea")
                            Log.e(ContentValues.TAG, "fullAddress: $fullAddress")
                            with(binding) {
                                etAddress1.setText("$subAdminArea $subLocality")
                                etAddress2.setText(subAdminArea)
                                edtCity.setText(locality)
                                edtZipCode.setText(postalCode)
                            }

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
        } else {
            askPermission()
        }
    }



    companion object {
        var postalCodeNew = ""
    }

    private fun askPermission() {
        ActivityCompat.requestPermissions(
            context,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }
}