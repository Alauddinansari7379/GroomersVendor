package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.databinding.ActivityUploadOwnerIdentityBinding

class UploadOwnerIdentity : Common() {
    private val binding by lazy { ActivityUploadOwnerIdentityBinding.inflate(layoutInflater) }
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
        val selectedCity = intent.getStringExtra("SELECTED_CITY")
        val selectedZip = intent.getStringExtra("SELECTED_ZIP")
        // Get the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE

        // Update the status bar color to match the background color
        updateStatusBarColor(backgroundColor)
        setupSpinners()
        binding.btnContinue.setOnClickListener {
            val selectedIdProof = binding.spinnerIdProof.selectedItem.toString()


            if (selectedIdProof == "Select City") {
                Toast.makeText(this, "Please select a city", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this@UploadOwnerIdentity, Register3::class.java)
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
            startActivity(Intent(this@UploadOwnerIdentity,Register3::class.java)) }
    }
    private fun setupSpinners() {
        val cityList = listOf("Select City", "Calgary", "Toronto", "Vancouver")
        val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cityList)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerIdProof.adapter = cityAdapter
    }
}