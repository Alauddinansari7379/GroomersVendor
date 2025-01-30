package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ArrayAdapter
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.databinding.ActivityUploadOwnerIdentityBinding

class UploadOwnerIdentity : Common() {
    private val binding by lazy { ActivityUploadOwnerIdentityBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Get the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE

        // Update the status bar color to match the background color
        updateStatusBarColor(backgroundColor)
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