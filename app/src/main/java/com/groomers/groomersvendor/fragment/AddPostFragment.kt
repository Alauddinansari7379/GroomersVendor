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
    private var selectedImageUri: Uri? = null
    private val viewModelService: ServiceViewModel by viewModels()
    private val viewModel by lazy {
        (requireActivity().application as MyApplication).createServiceViewModel
    }
    private val parts: MutableList<MultipartBody.Part> = mutableListOf()

    // Define image picker activity result launcher
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                binding.imageViewPreview.setImageURI(it)
                val imagePart = createMultipartFromUri(it)
//                viewModel.images = listOf(imagePart)
                handleImageSelection(selectedImageUri!!)
                binding.imageViewPreview.visibility = View.VISIBLE
            }
        }

    private fun handleImageSelection(uri: Uri) {
        val imagePart = createMultipartFromUri(requireContext(), uri)
        if (imagePart != null) {
            parts.add(imagePart)
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
                "image[]",
                file.name,
                body
            ) // Use "image[]" for multiple uploads
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun ContentResolver.getFileName(uri: Uri): String {
        var name = "temp_image"
        val cursor = query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && it.moveToFirst()) {
                name = it.getString(nameIndex)
            }
        }
        return name
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentAddPostBinding.inflate(inflater, container, false)
            .also { _binding = it }.root
    }

    // Example lists for Service Type and Category
    private val serviceTypeList =
        listOf("Select service type", "Cleaning", "Delivery", "Repair", "Maintenance")
    private val categoryList =
        listOf("Select category", "Household", "Electronics", "Furniture", "Apparel")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postId = arguments?.getString("postId")
        // Button click to add image

        if (postId != null && postId.isNotEmpty()) {
            viewModel.editFlag = postId
            sessionManager.accessToken?.let { token ->
                lifecycleScope.launch {
                    viewModelService.getSingleService(token, postId)
                    observeViewModel()
                }
            } ?: run {
                Toast.makeText(requireContext(), "Error: Missing Token", Toast.LENGTH_LONG).show()
            }
        }
        binding.btnAddImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Set up spinners with lists
        setupSpinners()

        // Set up calendar selection for slot time
        binding.editTextSlotTime.setOnClickListener {
            openDatePickerDialog()
        }
        binding.date.setOnClickListener {
            openDatePickerDialog1()
        }
        binding.btnAddPost.setOnClickListener {
            val selectedCategory = binding.spinnerCategory.selectedItem.toString()
            val selectedServiceType = binding.spinnerServiceType.selectedItem.toString()
            viewModel.category = selectedCategory
            viewModel.serviceType = selectedServiceType



            viewModel.description = binding.editTextDescription.text.toString()
            viewModel.price = binding.edPrice.text.toString()
            viewModel.address = binding.editTextAddress.text.toString()
            viewModel.time = binding.edtServiceTime.text.toString()
            viewModel.date = binding.date.text.toString()
            viewModel.slot_time = binding.editTextSlotTime.text.toString()




            if (viewModel.description!!.isEmpty()) {
                binding.editTextDescription.error = "Please enter description"
                binding.editTextDescription.requestFocus()
                return@setOnClickListener
            }
            if (viewModel.images.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please select service image", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (viewModel.price!!.isEmpty()) {
                binding.edPrice.error = "Please enter price"
                binding.edPrice.requestFocus()
                return@setOnClickListener
            }
            if (viewModel.time!!.isEmpty()) {
                binding.edtServiceTime.error = "Please enter service time"
                binding.edtServiceTime.requestFocus()
                return@setOnClickListener
            }
            if (selectedServiceType == "Select service type") {
                Toast.makeText(requireContext(), "Please select a service type", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (viewModel.date!!.isEmpty()) {
                binding.date.error = "Please enter service date"
                binding.date.requestFocus()
                return@setOnClickListener
            }
            if (selectedCategory == "Select category") {
                Toast.makeText(requireContext(), "Please select a category", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }


            if (viewModel.slot_time!!.isEmpty()) {
                binding.editTextSlotTime.error = "Please enter your date"
                binding.editTextSlotTime.requestFocus()
                return@setOnClickListener
            }
            if (viewModel.address!!.isEmpty()) {
                binding.editTextAddress.error = "Please enter address"
                binding.editTextAddress.requestFocus()
                return@setOnClickListener
            }
            val intent = Intent(requireContext(), AddServiceDetails::class.java)
            startActivity(intent)
        }
        binding.btnAlreadyAdded.setOnClickListener {
            val intent = Intent(requireContext(), ServiceList::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModelService.isLoading.observe(requireActivity()) { isLoading ->
            if (isLoading) CustomLoader.showLoaderDialog(requireContext())
            else CustomLoader.hideLoaderDialog()
        }

        viewModelService.errorMessage.observe(requireActivity()) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModelService.modelSingleService.observe(requireActivity()) { response ->
            response?.result?.let { services ->
                if (services.isNotEmpty()) {
                    val service = services.first() // Get the first item in the list

                    viewModel.category = service.category ?: ""
                    viewModel.serviceType = service.serviceType ?: ""
                    viewModel.description = service.description ?: ""
                    viewModel.price = service.price?.toString() ?: ""
                    viewModel.address = service.address ?: ""
                    viewModel.time = service.time ?: ""
                    viewModel.date = service.date ?: ""
                    viewModel.slot_time = service.slot_time ?: ""
//                    viewModel.discount = service.discount ?: ""
                    viewModel.serviceName = service.serviceName ?: ""
                    viewModel.user_type = service.user_type ?: ""
                    viewModel.slot_time = service.slot_time ?: ""
                    viewModel.imageUrl = service.image
                    binding.editTextDescription.setText(viewModel.description)
                    binding.edPrice.setText(viewModel.price)
                    binding.edtServiceTime.setText(viewModel.time)
                    binding.date.setText(viewModel.date)
                    binding.editTextSlotTime.setText(viewModel.slot_time)
                    binding.editTextAddress.setText(viewModel.address)

                    Glide.with(requireContext())
                        .load("https://groomers.co.in/public/uploads/" + viewModel.images)
                        .into(binding.imageViewPreview)


                }}


            }


        }

        private fun setupSpinners() {
            // Set up the adapter for Service Type spinner using the serviceTypeList
            val serviceTypeAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                serviceTypeList
            )
            serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerServiceType.adapter = serviceTypeAdapter

            // Set up the adapter for Category spinner using the categoryList
            val categoryAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categoryList
            )
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategory.adapter = categoryAdapter
        }

        private fun openDatePickerDialog() {
            // Get current date
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Open DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Set selected date in EditText
                    val selectedDate = "$selectedYear/${selectedMonth + 1}/$selectedDay"
                    binding.editTextSlotTime.setText(selectedDate)
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        private fun openDatePickerDialog1() {
            // Get current date
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Open DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Set selected date in EditText
                    val selectedDate = "$selectedYear/${selectedMonth + 1}/$selectedDay"
                    binding.date.setText(selectedDate)
                    viewModel.date = selectedDate
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        private fun createMultipartFromUri(uri: Uri): MultipartBody.Part {
            val contentResolver = requireContext().contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(requireContext().cacheDir, "upload_image.jpg")

            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData("images", file.name, requestBody)
        }
    }
