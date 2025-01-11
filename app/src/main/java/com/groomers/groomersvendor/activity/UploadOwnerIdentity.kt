package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.groomers.groomersvendor.databinding.ActivityUploadOwnerIdentityBinding

class UploadOwnerIdentity : AppCompatActivity() {
    private val binding by lazy { ActivityUploadOwnerIdentityBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setupSpinners()
        binding.btnContinue.setOnClickListener { startActivity(Intent(this@UploadOwnerIdentity,Register3::class.java)) }
    }
    private fun setupSpinners() {
        val cityList = listOf("Select City", "Calgary", "Toronto", "Vancouver")
        val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cityList)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerIdProof.adapter = cityAdapter
    }
}