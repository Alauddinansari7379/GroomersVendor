package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.databinding.ActivityRegister3Binding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.helper.Toastic
import com.groomers.groomersvendor.retrofit.ApiServiceProvider
import com.groomers.groomersvendor.viewmodel.MyApplication
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class Register3 : Common() {

    private val binding by lazy { ActivityRegister3Binding.inflate(layoutInflater) }

    private val viewModel by lazy {
        (application as MyApplication).registerViewModel
    }

    private var changeTextColor: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE
        // Update the status bar color to match the background color
        updateStatusBarColor(backgroundColor)

        // Retrieve data from intent
        val businessCategory = viewModel.businessCategory ?: ""
        val businessName = viewModel.businessName ?: ""
        val name = viewModel.yourName ?: ""
        val mobile = viewModel.mobile ?: ""
        val email = viewModel.email ?: ""
        val password = viewModel.password ?: ""
        val teamSize = viewModel.teamSize ?: ""
        val city = viewModel.city ?: ""
        val zipcode = viewModel.zipcode ?: ""
        val idproofImagePath = viewModel.idProofImagePath ?: ""
        val accountName = viewModel.accountName ?: ""
        val accountNo = viewModel.accountNo ?: ""
        val bankName = viewModel.bankName ?: ""
        val ifsc = viewModel.ifsc ?: ""
        val branchName = viewModel.branchName ?: ""
        val service = viewModel.services ?: ""
        val latitude = viewModel.latitude.toString() ?: ""
        val longitude = viewModel.longitude.toString() ?: ""
        val aboutBusiness = viewModel.aboutBusiness.toString() ?: ""
        val adrdress=viewModel.address1+" "+viewModel.address2+" "+viewModel.city+" "+viewModel.zipcode
        // Convert image path to MultipartBody.Part
        val shopAgreement = prepareFilePart("shop_agreement", idproofImagePath)


        // Handle the continue button click
        binding.btnContinue3.setOnClickListener {
            if (validateBusinessHours()) {
                // Make the API call to register the user
                registerUser(
                    name,
                    mobile,
                    email,
                    password,
                    teamSize,
                    businessName,
                    businessCategory,
                    city,
                    zipcode,
                    shopAgreement,
                    businessName,
                    accountName,
                    accountNo,
                    bankName,
                    ifsc,
                    branchName,
                    adrdress,
                    service,
                    latitude,
                    longitude,
                    aboutBusiness,
                )
            } else {
                Toast.makeText(this, "Please select at least one working day.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // Observe loading state
        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                CustomLoader.showLoaderDialog(this@Register3)
            } else {
                CustomLoader.hideLoaderDialog()
            }
        })

        // Observe error message
        viewModel.errorMessage.observe(this, Observer { errorMessage ->

            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })

        // Observe success response
        viewModel.modelRegister.observe(this, Observer { response ->
            if (response != null && response.status == 1) {
                // Handle success - Show success message or navigate to another screen

                Toastic.toastic(
                    context = this@Register3,
                    message = "Registration Successful",
                    duration = Toastic.LENGTH_SHORT,
                    type = Toastic.SUCCESS,
                    isIconAnimated = true,
                    textColor = if (changeTextColor) Color.BLUE else null,
                ).show()
                // Navigate to the next screen if needed
                val intent = Intent(this@Register3, RegisterSuccess::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        })
    }

    private fun prepareFilePart(partName: String, filePath: String): MultipartBody.Part {
        val file = File(filePath)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }


    private fun validateBusinessHours(): Boolean {
        // Implement your logic to validate business hours (e.g., check if any day is selected)
        return true
    }

    private fun registerUser(
        name: String, mobile: String, email: String, password: String, teamSize: String,
        businessName: String, businessCategory: String, city: String, zipcode: String,
        shopAgreement: MultipartBody.Part,
        businessName1: String,
        accountName: String,
        accountNo: String,
        bankName: String,
        ifsc: String,
        branchName: String,
        adrdress: String,
        service: String,
        latitude: String,
        longitude: String,
        aboutBusiness: String,
    ) {

        val apiService = ApiServiceProvider.getApiService() // Initialize ApiService
        viewModel.registerUser(
            apiService,
            name, mobile, email, password, password, // passwordConfirmation
            "vendor", businessName, businessCategory, aboutBusiness,
            teamSize, adrdress, city, zipcode, "",service ,
            latitude,  longitude, shopAgreement, "1", businessName,accountName,accountNo,bankName,ifsc,branchName
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearRegisterData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.clearRegisterData()
    }
}
