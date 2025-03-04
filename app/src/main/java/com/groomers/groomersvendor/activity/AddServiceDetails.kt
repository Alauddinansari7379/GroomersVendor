package com.groomers.groomersvendor.activity

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.groomers.groomersvendor.MainActivity
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityAddServiceDetailsBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.retrofit.ApiService
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.groomers.groomersvendor.viewmodel.CreateServiceViewModel
import com.groomers.groomersvendor.viewmodel.MyApplication

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddServiceDetails : AppCompatActivity() {
    private val viewModel by lazy {
        (application as MyApplication).createServiceViewModel
    }

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var sessionManager: SessionManager

    private val binding by lazy { ActivityAddServiceDetailsBinding.inflate(layoutInflater) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupInitialValues()
        setupSpinners()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupInitialValues() {
        viewModel.apply {
            binding.etServiceName.setText(serviceName)
            binding.etDiscount.setText(price)
            binding.etDuration.setText(time)

            if (!editFlag.isNullOrEmpty()) {
                binding.btnContinue3.text = "Update"
            }
        }
    }

    private fun setupSpinners() {
        val userTypeList = listOf("Male", "Female", "Pet")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypeList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerUserType.adapter = adapter

        // Set selected user type
        val selectedIndex = userTypeList.indexOf(viewModel.user_type)
        if (selectedIndex != -1) {
            binding.spinnerUserType.setSelection(selectedIndex)
        }
    }

    private fun setupClickListeners() {
        binding.etDuration.setOnClickListener {
            TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                    binding.etDuration.setText(formattedTime)
                },
                0, 0, true
            ).show()
        }

        binding.btnContinue3.setOnClickListener {
            val serviceName = binding.etServiceName.text.toString().trim()
            val userType = binding.spinnerUserType.selectedItem.toString()
            val discount = binding.etDiscount.text.toString().trim()
            val serviceDuration = binding.etDuration.text.toString().trim()

//            if (serviceName.isEmpty()) {
//                binding.etServiceName.error = "Please enter service name"
//                binding.etServiceName.requestFocus()
//                return@setOnClickListener
//            }
            if (discount.isEmpty()) {
                binding.etDiscount.error = "Please enter discount"
                binding.etDiscount.requestFocus()
                return@setOnClickListener
            }
            if (serviceDuration.isEmpty()) {
                binding.etDuration.error = "Please enter service duration"
                binding.etDuration.requestFocus()
                return@setOnClickListener
            }

            // Perform create or update action
            sessionManager.accessToken?.let { token ->
                viewModel.images?.let { imageList ->
                    if (!viewModel.editFlag.isNullOrEmpty()) {
                        viewModel.updateService(
                            token,
                            apiService,
                            serviceName,
                            viewModel.description ?: "",
                            viewModel.price ?: "",
                            viewModel.time ?: "",
                            viewModel.serviceType ?: "",
                            viewModel.date ?: "",
                            viewModel.category ?: "",
                            viewModel.slot_time ?: "",
                            viewModel.address ?: "",
                            userType,
                            imageList,
                            viewModel.editFlag!!
                        )
                    } else {
                        viewModel.createService(
                            token,
                            apiService,
                            serviceName,
                            viewModel.description ?: "",
                            viewModel.price ?: "",
                            viewModel.time ?: "",
                            viewModel.serviceType ?: "",
                            viewModel.date ?: "",
                            viewModel.category ?: "",
                            viewModel.slot_time ?: "",
                            viewModel.address ?: "",
                            userType,
                            imageList
                        )
                    }
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.modelCreateService.observe(this) {
//            clearViewModelData()

            Toast.makeText(this, "Service added successfully.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("openFragment", "HomeFragment")
            }
            startActivity(intent)
            finish()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                CustomLoader.showLoaderDialog(this)
                viewModel.clearServiceData()
            } else CustomLoader.hideLoaderDialog()
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearViewModelData() {
        viewModel.apply {
            category = ""
            serviceType = ""
            description = ""
            price = ""
            address = ""
            time = ""
            date = ""
            slot_time = ""
            serviceName = ""
            user_type = ""
            imageUrl = null
            images = null
            editFlag = ""
//            if (this is CreateServiceViewModel) {
//                clearServiceData()
//            }
        }
    }
}
