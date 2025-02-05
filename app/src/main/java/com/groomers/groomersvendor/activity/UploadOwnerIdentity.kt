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
import androidx.appcompat.app.AppCompatActivity
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.databinding.ActivityUploadOwnerIdentityBinding
import java.io.File
class UploadOwnerIdentity : Common() {
    private val binding by lazy { ActivityUploadOwnerIdentityBinding.inflate(layoutInflater) }
    private var selectedImagePath: String? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            binding.previewImage.setImageURI(it)
            binding.previewImage.visibility = View.VISIBLE
            binding.imagePickerIcon.visibility = View.GONE
            binding.textUploadHint.visibility = View.GONE
            selectedImagePath = getFilePathFromUri(it)
        }
    }

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

        // Get and set the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE
        updateStatusBarColor(backgroundColor)

        setupSpinners()

        binding.capturePhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnContinue.setOnClickListener {
            val selectedIdProof = binding.spinnerIdProof.selectedItem.toString()

            if (selectedIdProof == "Select City") {
                Toast.makeText(this, "Please select a city", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedImagePath == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val intent = Intent(this@UploadOwnerIdentity, Register3::class.java).apply {
                putExtra("SELECTED_CITY", selectedCity)
                putExtra("SELECTED_ZIP", selectedZip)
                putExtra("category", category)
                putExtra("businessName", businessName)
                putExtra("yourName", yourName)
                putExtra("phoneNO", phoneNO)
                putExtra("userId", userId)
                putExtra("email", email)
                putExtra("password", password)
                putExtra("teamSize", teamSize)
                putExtra("imagePath", selectedImagePath)
            }
            startActivity(intent)
        }
    }

    private fun setupSpinners() {
        val cityList = listOf("Select City", "Calgary", "Toronto", "Vancouver")
        val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cityList)
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

