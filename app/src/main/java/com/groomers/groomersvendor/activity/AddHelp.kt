package com.groomers.groomersvendor.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.databinding.ActivityAddHelpBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.helper.Toastic
import com.groomers.groomersvendor.retrofit.ApiService
import com.groomers.groomersvendor.viewmodel.AddHelpViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class AddHelp : Common() {
    private var selectedImagePath: String? = null
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                binding.imagePickerIcon.setImageURI(it)
                selectedImagePath = getFilePathFromUri(it)
            }

        }

    @Inject
    lateinit var apiService: ApiService
    private val viewModel: AddHelpViewModel by viewModels()
    private val binding by lazy { ActivityAddHelpBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Get the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE

        // Update the status bar color to match the background color
        updateStatusBarColor(backgroundColor)
        binding.btnChooseImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.btnSubmit.setOnClickListener {
            val name = binding.etName.text.toString()
            val mobile = binding.etMobile.text.toString()
            val query = binding.etQuery.text.toString()
            val description = binding.etDescription.text.toString()
            if (name.isEmpty()) {
                binding.etName.error = "Please enter your name"
                binding.etName.requestFocus()
                return@setOnClickListener
            }
            if (mobile.isEmpty()) {
                binding.etMobile.error = "Please enter your mobile no"
                binding.etMobile.requestFocus()
                return@setOnClickListener
            }
            if (query.isEmpty()) {
                binding.etQuery.error = "Please enter your query"
                binding.etQuery.requestFocus()
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                binding.etDescription.error = "Please enter description"
                binding.etDescription.requestFocus()
                return@setOnClickListener
            }
            if (selectedImagePath == null) {
                Toastic.toastic(
                    context = this@AddHelp,
                    message = "Please select the image",
                    duration = Toastic.LENGTH_SHORT,
                    type = Toastic.WARNING,
                    isIconAnimated = true,
                    textColor = if (false) Color.BLUE else null,
                ).show()
                return@setOnClickListener
            }
            val imageUrl = prepareFilePart("image", selectedImagePath!!)
            viewModel.addHelp(name, mobile, query, description, imageUrl)
        }
        // Observe isLoading to show/hide progress
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                CustomLoader.showLoaderDialog(this)
            } else {
                CustomLoader.hideLoaderDialog()
            }
        }

        // Observe the result of the login attempt
        viewModel.modelAddHelp.observe(this) { response ->
            if (response!!.status == 1) {
                Toastic.toastic(
                    context = this@AddHelp,
                    message = "Data submitted Successfully",
                    duration = Toastic.LENGTH_SHORT,
                    type = Toastic.SUCCESS,
                    isIconAnimated = true,
                    textColor = if (false) Color.BLUE else null,
                ).show()
                finish()
            }
        }
        // Observe error message if login fails
        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage!!.isNotEmpty()) {
                Toastic.toastic(
                    context = this@AddHelp,
                    message = errorMessage,
                    duration = Toastic.LENGTH_SHORT,
                    type = Toastic.ERROR,
                    isIconAnimated = true,
                    textColor = if (false) Color.BLUE else null,
                ).show()
            }
        }

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

    private fun prepareFilePart(partName: String, filePath: String): MultipartBody.Part {
        val file = File(filePath)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.clearRegisterData()
    }
}