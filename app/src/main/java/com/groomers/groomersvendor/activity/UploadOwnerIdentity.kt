package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.databinding.ActivityUploadOwnerIdentityBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.retrofit.ApiServiceProvider
import com.groomers.groomersvendor.viewmodel.LocationViewModel
import com.groomers.groomersvendor.viewmodel.MyApplication
import com.groomers.groomersvendor.viewmodel.RegisterViewModel
import java.io.File

class UploadOwnerIdentity : Common() {
    private val binding by lazy { ActivityUploadOwnerIdentityBinding.inflate(layoutInflater) }
    private var selectedImagePath: String? = null
    private val viewModel by lazy {
        (application as MyApplication).registerViewModel
    }
    private val locationViewModel: LocationViewModel by viewModels()
    private val context = this@UploadOwnerIdentity

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                binding.previewImage.setImageURI(it)
                binding.previewImage.visibility = View.VISIBLE
                binding.imagePickerIcon.visibility = View.GONE
                binding.textUploadHint.visibility = View.GONE
                selectedImagePath = getFilePathFromUri(it)
                viewModel.idProofImagePath = selectedImagePath
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get and set the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE
        updateStatusBarColor(backgroundColor)
        val apiService = ApiServiceProvider.getApiService()
        locationViewModel.getState(apiService)
        // Observe isLoading to show/hide progress
        locationViewModel.isLoading.observe(context) { isLoading ->
            if (isLoading) {
                CustomLoader.showLoaderDialog(context)
            } else {
                CustomLoader.hideLoaderDialog()
            }
        }
        // Observe error message if login fails
        locationViewModel.errorMessage.observe(context) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        setupSpinners()

        binding.capturePhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnContinue.setOnClickListener {
            val selectedIdProof = binding.spinnerIdProof.selectedItem.toString()

            if (selectedIdProof == "Select id proof") {
                Toast.makeText(this, "Please select a id proof", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedImagePath == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val intent = Intent(this@UploadOwnerIdentity, BankInformation::class.java)
            startActivity(intent)
        }
    }

    private fun setupSpinners() {

        val zipCodeList = listOf("Select id proof","Passport",
                "National Identity Card (if applicable)",
                "Driver’s License",
                "Voter ID Card",
                "Social Security Card (USA) / Aadhaar Card (India) / Similar National ID",
                "Birth Certificate",
                "Government Employee ID",
                "PAN Card (India) / Taxpayer Identification Number (TIN)",
                "Student ID (for academic purposes)",
                "Work Permit / Residence Card (for non-citizens in some countries)")
                val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, zipCodeList)
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerIdProof.adapter = cityAdapter

    }

    private fun getFilePathFromUri(uri: Uri): String? {
        var filePath: String? = null
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            val fileName = cursor.getString(columnIndex)
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(cacheDir, fileName)
            file.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
            filePath = file.absolutePath
        }
        return filePath
    }
}

