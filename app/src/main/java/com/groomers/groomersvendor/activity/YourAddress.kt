package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityYourAddressBinding

class YourAddress : AppCompatActivity() {
    private val binding by lazy { ActivityYourAddressBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnContinue.setOnClickListener {
            startActivity(
                Intent(
                    this@YourAddress,
                    UploadOwnerIdentity::class.java
                )
            )
        }
        setupSpinners()
    }

    private fun setupSpinners() {
        val cityList = listOf("Select City", "Calgary", "Toronto", "Vancouver")
        val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cityList)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCity.adapter = cityAdapter

// Populate ZIP code spinner
        val zipCodeList = listOf("Select ZIP Code", "T3C", "M5H", "V5K")
        val zipCodeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, zipCodeList)
        zipCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerZip.adapter = zipCodeAdapter
    }
}