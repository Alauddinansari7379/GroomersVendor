package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.databinding.ActivityRegister3Binding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.retrofit.ApiServiceProvider
import com.groomers.groomersvendor.viewmodel.RegisterViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class Register3 : Common() {

    private val binding by lazy { ActivityRegister3Binding.inflate(layoutInflater) }
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE

        // Update the status bar color to match the background color
        updateStatusBarColor(backgroundColor)
        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        // Retrieve data from intent
        val businessCategory = intent.getStringExtra("category") ?: ""
        val businessName = intent.getStringExtra("businessName") ?: ""
        val name = intent.getStringExtra("yourName") ?: ""
        val mobile = intent.getStringExtra("phoneNO") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val password = intent.getStringExtra("password") ?: ""
        val teamSize = intent.getStringExtra("teamSize") ?: ""
        val city = intent.getStringExtra("SELECTED_CITY") ?: ""
        val zipcode = intent.getStringExtra("SELECTED_ZIP") ?: ""
        val idproofImagePath = intent.getStringExtra("imagePath") ?: ""

        // Convert image path to MultipartBody.Part
        val shopAgreement = prepareFilePart("shop_agreement", idproofImagePath)


        // Handle the continue button click
        binding.btnContinue3.setOnClickListener {
            if (validateBusinessHours()) {
                // Make the API call to register the user
                registerUser(name, mobile, email, password, teamSize, businessName, businessCategory, city, zipcode, shopAgreement)
            } else {
                Toast.makeText(this, "Please select at least one working day.", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        })

        // Observe success response
        viewModel.modelRegister.observe(this, Observer { response ->
            // Handle success - Show success message or navigate to another screen
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
            // Navigate to the next screen if needed
            startActivity(Intent(this@Register3,RegisterSuccess::class.java))
            finish()
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
        shopAgreement: MultipartBody.Part
    ) {

        val apiService = ApiServiceProvider.getApiService() // Initialize ApiService
        viewModel.registerUser(
            apiService,
            name, mobile, email, password, password, // passwordConfirmation
            "vendor", businessName, businessCategory, "Best shop in town",
            teamSize, "123 Street", city, zipcode, "Aadhar", "Haircut, Spa",
            "40.7128", "-74.0060", shopAgreement
        )
    }
}
