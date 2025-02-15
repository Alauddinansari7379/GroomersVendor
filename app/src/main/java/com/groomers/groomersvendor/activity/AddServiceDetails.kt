package com.groomers.groomersvendor.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.groomers.groomersvendor.MainActivity
import com.groomers.groomersvendor.databinding.ActivityAddServiceDetailsBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.retrofit.ApiService
import com.groomers.groomersvendor.sharedpreferences.SessionManager
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
    private val context = this@AddServiceDetails
    private val binding by lazy { ActivityAddServiceDetailsBinding.inflate(layoutInflater) }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val date = viewModel.date ?: ""
        val description = viewModel.description ?: ""
        val serviceType = viewModel.serviceType ?: ""
        val image = viewModel.image
        val time = viewModel.time ?: ""
        val category = viewModel.category ?: ""
        val price = viewModel.price ?: ""
        val serviceName = viewModel.serviceName ?: ""
        val userType = viewModel.user_type ?: ""
        val editFlag = viewModel.editFlag ?: ""
        if (serviceName.isNotEmpty() && userType.isNotEmpty()) {
            binding.etServiceName.setText(serviceName)
            binding.etUserType.setText(userType)
        }
        if (editFlag.isNotEmpty()){
            binding.btnContinue3.text = "Update"
        }

        binding.btnContinue3.setOnClickListener {
            val serviceName = binding.etServiceName.text.toString()
            val userType = binding.etUserType.text.toString()
            val address = viewModel.address ?: ""
            val slotTime = viewModel.slot_time ?: ""
            val discount = binding.etDiscount.text.toString()
            val serviceDuration = binding.etDuration.text.toString()
            if (userType.isEmpty()) {
                binding.etUserType.error = "Please enter user type"
                binding.etUserType.requestFocus()
                return@setOnClickListener
            }
            if (serviceName.isEmpty()) {
                binding.etServiceName.error = "Please enter service name"
                binding.etServiceName.requestFocus()
                return@setOnClickListener
            }
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
            if (editFlag.isNotEmpty()){
                viewModel.updateService(
                    sessionManager.accessToken!!,
                    apiService,
                    serviceName,
                    description,
                    price,
                    time,
                    serviceType,
                    date,
                    category,
                    slotTime,
                    address,
                    userType,
                    image
                )
            }else {
                viewModel.createService(
                    sessionManager.accessToken!!,
                    apiService,
                    serviceName,
                    description,
                    price,
                    time,
                    serviceType,
                    date,
                    category,
                    slotTime,
                    address,
                    userType,
                    image
                )
            }
        }
        // Observe success response
        viewModel.modelCreateService.observe(this, Observer { response ->
            // Handle success - Show success message or navigate to another screen
            Toast.makeText(this, "Service added Successfully.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("openFragment", "HomeFragment") // Pass a flag
            startActivity(intent)
            finish()
        })
        // Observe loading state
        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                CustomLoader.showLoaderDialog(this@AddServiceDetails)
            } else {
                CustomLoader.hideLoaderDialog()
            }
        })

        // Observe error message
        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        })
    }
}