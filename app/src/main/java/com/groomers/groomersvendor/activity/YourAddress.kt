package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityYourAddressBinding

class YourAddress : Common() {
    private val binding by lazy { ActivityYourAddressBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val category = intent.getStringExtra("category")
        val businessName = intent.getStringExtra("businessName")
        val yourName = intent.getStringExtra("yourName")
        val phoneNO = intent.getStringExtra("phoneNO")
        val userId = intent.getStringExtra("userId")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        val teamSize = intent.getStringExtra("teamSize")

        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE
        updateStatusBarColor(backgroundColor)

        binding.btnContinue.setOnClickListener {
            val selectedCity = binding.spinnerCity.selectedItem.toString()
            val selectedZip = binding.spinnerZip.selectedItem.toString()


            if (selectedCity == "Select City") {
                Toast.makeText(this, "Please select a city", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedZip == "Select ZIP Code") {
                Toast.makeText(this, "Please select a ZIP Code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this@YourAddress, UploadOwnerIdentity::class.java)
            intent.putExtra("SELECTED_CITY", selectedCity)
            intent.putExtra("SELECTED_ZIP", selectedZip)
            intent.putExtra("category", category)
            intent.putExtra("businessName", businessName)
            intent.putExtra("yourName", yourName)
            intent.putExtra("phoneNO", phoneNO)
            intent.putExtra("userId", userId)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            intent.putExtra("teamSize", teamSize)
            startActivity(intent)
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