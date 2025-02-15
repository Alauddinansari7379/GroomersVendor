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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
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
    lateinit var parts: MultipartBody.Part

    private var imageUri: Uri? = null

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri?.let {
                    binding.imageViewPreview.setImageURI(it)
                    handleImageSelection1(it)
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

            viewModel.images.toString().isNullOrEmpty() -> showError("Please select a service image")
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
        createMultipartFromUri(requireContext(),uri)?.let {
            //parts.add(it)
            parts=it
            viewModel.images = parts
        }
    }
    private fun handleImageSelection1(uri: Uri) {
        createMultipartFromUri1(requireContext(),uri)?.let {
            //parts.add(it)
            parts=it
            viewModel.images = parts
        }
    }

    private fun createMultipartFromUri(context: Context, uri: Uri): MultipartBody.Part? {
        return try {
            val contentResolver = context.contentResolver
            val parcelFileDescriptor =
                contentResolver.openFileDescriptor(uri, "r", null) ?: return null
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(
                context.cacheDir,
                contentResolver.getFileName(uri)
            ) // Ensure filename uniqueness
            val outputStream = FileOutputStream(file)

            inputStream.copyTo(outputStream)
            outputStream.close()
            inputStream.close()

            val body = UploadRequestBody(file, "image", context)
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ) // Use "image[]" for multiple uploads
        } catch (e: Exception) {
            e.printStackTrace()
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

                val imageUrl = "https://groomers.co.in/public/uploads/${viewModel.imageUrl}"

                Glide.with(requireContext())
                    .load(imageUrl)
                    .into(binding.imageViewPreview)

                // Download the image and convert it to MultipartBody
                downloadImageAndConvertToMultipart(imageUrl)
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

    private fun createMultipartFromUri1(context: Context, uri: Uri): MultipartBody.Part? {
        return try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")

            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            inputStream.close()

            val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", file.name, requestBody)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    private fun downloadImageAndConvertToMultipart(imageUrl: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val file = File(requireContext().cacheDir, "downloaded_image.jpg")
                    val outputStream = FileOutputStream(file)

                    inputStream.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }

                    val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestBody)

                    withContext(Dispatchers.Main) {
                        viewModel.images = multipartBody
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showError("Failed to download image")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    showError("Error downloading image: ${e.localizedMessage}")
                }
            }
        }
    }

}
