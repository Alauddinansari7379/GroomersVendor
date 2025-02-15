package com.groomers.groomersvendor.fragment

import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.AddServiceDetails
import com.groomers.groomersvendor.activity.ServiceList
import com.groomers.groomersvendor.adapter.ServiceAdapter
import com.groomers.groomersvendor.databinding.FragmentAddPostBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.helper.UploadRequestBody
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.groomers.groomersvendor.viewmodel.MyApplication
import com.groomers.groomersvendor.viewmodel.ServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class AddPostFragment : Fragment(R.layout.fragment_add_post) {
    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sessionManager: SessionManager
    private var selectService : Boolean = false
    private var selectedImageUri: Uri? = null
    private val viewModelService: ServiceViewModel by viewModels()
    private val viewModel by lazy {
        (requireActivity().application as MyApplication).createServiceViewModel
    }
    private val parts: MutableList<MultipartBody.Part> = mutableListOf()

    private var imageUri: Uri? = null

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri?.let {
                    binding.imageViewPreview.setImageURI(it)
                    handleImageSelection(it)
                    binding.imageViewPreview.visibility = View.VISIBLE
                }
            } else {
                showError("Failed to capture image")
            }
        }
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                binding.imageViewPreview.setImageURI(it)
                handleImageSelection(it)
                binding.imageViewPreview.visibility = View.VISIBLE
//                binding.textImageSelected.text = "Image selected successfully ✅"
//                binding.textImageSelected.visibility = View.VISIBLE
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentAddPostBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postId = arguments?.getString("postId")

        if (!postId.isNullOrEmpty()) {
            viewModel.editFlag = postId
            sessionManager.accessToken?.let { token ->
                lifecycleScope.launch {
                    viewModelService.getSingleService(token, postId)
                    observeViewModel()
                }
            } ?: showError("Error: Missing Token")
        }
        binding.llHaircut.setOnClickListener {
            selectService = true
            binding.llHaircut.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.selected_card)
        }
        binding.llPet.setOnClickListener {
            selectService = true
            binding.llPet.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.selected_card)
        }
        binding.llmMakeup.setOnClickListener {
            selectService = true
            binding.llmMakeup.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.selected_card)
        }
        binding.llbodyMassage.setOnClickListener {
            selectService = true
            binding.llbodyMassage.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.selected_card)
        }
        binding.llteeth.setOnClickListener {
            selectService = true
            binding.llteeth.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.selected_card)
        }
        binding.llMore.setOnClickListener {
            selectService = true
            binding.llMore.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.selected_card)
        }

        binding.layoutGallery.setOnClickListener { pickImageLauncher.launch("image/*") }
        binding.layoutCamera.setOnClickListener {
            imageUri = createImageUri()
            imageUri?.let { takePictureLauncher.launch(it) }
        }

        binding.date.setOnClickListener { openDatePickerDialog() }

        binding.btnAddPost.setOnClickListener { validateAndProceed() }

        binding.btnAlreadyAdded.setOnClickListener {
            startActivity(Intent(requireContext(), ServiceList::class.java))
        }
    }
    private fun createImageUri(): Uri? {
        val file = File(requireContext().cacheDir, "captured_image.jpg")

        // Ensure the cache directory exists
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }

        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            file
        )
    }

    private fun validateAndProceed() {
        if (!selectService) {
            showError("Please select a service before proceeding!")
            return
        }
        viewModel.apply {
            description = binding.editTextDescription.text.toString().trim()
            price = binding.edPrice.text.toString().trim()
            address = binding.editTextAddress.text.toString().trim()
            date = binding.date.text.toString().trim()
        }

        when {
            viewModel.description.isNullOrEmpty() -> showErrorField(
                binding.editTextDescription,
                "Please enter a description"
            )

            viewModel.images.isNullOrEmpty() -> showError("Please select a service image")
            viewModel.price.isNullOrEmpty() -> showErrorField(
                binding.edPrice,
                "Please enter a price"
            )

            viewModel.date.isNullOrEmpty() -> showErrorField(
                binding.date,
                "Please enter a service date"
            )

            viewModel.address.isNullOrEmpty() -> showErrorField(
                binding.editTextAddress,
                "Please enter an address"
            )

            else -> {
                startActivity(Intent(requireContext(), AddServiceDetails::class.java))
            }
        }
    }

    private fun handleImageSelection(uri: Uri) {
        createMultipartFromUri(uri)?.let {
            parts.add(it)
            viewModel.images = parts
        }
    }

    private fun createMultipartFromUri(uri: Uri): MultipartBody.Part? {
        return try {
            val contentResolver = requireContext().contentResolver
            val file = File(requireContext().cacheDir, contentResolver.getFileName(uri))
            contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            }

            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images", file.name, requestBody)
        } catch (e: Exception) {
            showError("Image selection failed: ${e.message}")
            null
        }
    }

    private fun openDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val formattedDate = "%04d-%02d-%02d".format(year, month + 1, day)
                binding.date.setText(formattedDate)
                viewModel.date = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate =
            System.currentTimeMillis() - 1000 // Disable past dates
        datePickerDialog.show()
    }

    private fun observeViewModel() {
        viewModelService.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) CustomLoader.showLoaderDialog(requireContext())
            else CustomLoader.hideLoaderDialog()
        }

        viewModelService.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            showError(errorMessage)
        }

        viewModelService.modelSingleService.observe(viewLifecycleOwner) { response ->
            response?.result?.firstOrNull()?.let { service ->
                viewModel.apply {
                    category = service.category ?: ""
                    serviceType = service.serviceType ?: ""
                    description = service.description ?: ""
                    price = service.price?.toString() ?: ""
                    address = service.address ?: ""
                    time = service.time ?: ""
                    date = service.date ?: ""
                    slot_time = service.slot_time ?: ""
                    serviceName = service.serviceName ?: ""
                    user_type = service.user_type ?: ""
                    imageUrl = service.image
                }
                binding.editTextDescription.setText(viewModel.description)
                binding.edPrice.setText(viewModel.price)
                binding.date.setText(viewModel.date)
                binding.editTextAddress.setText(viewModel.address)

                Glide.with(requireContext())
                    .load("https://groomers.co.in/public/uploads/${viewModel.imageUrl}")
                    .into(binding.imageViewPreview)
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorField(field: EditText, message: String) {
        field.error = message
        field.requestFocus()
    }

    private fun ContentResolver.getFileName(uri: Uri): String {
        return query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex != -1) cursor.getString(nameIndex) else "temp_image.jpg"
        } ?: "temp_image.jpg"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
