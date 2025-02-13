package com.groomers.groomersvendor.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import androidx.fragment.app.Fragment
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.AddServiceDetails
import com.groomers.groomersvendor.activity.ServiceList
import com.groomers.groomersvendor.databinding.FragmentAddPostBinding
import com.groomers.groomersvendor.viewmodel.MyApplication
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Calendar

@AndroidEntryPoint
class AddPostFragment : Fragment(R.layout.fragment_add_post) {
    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null
    private val viewModel by lazy {
        (requireActivity().application as MyApplication).createServiceViewModel
    }

    // Define image picker activity result launcher
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                binding.imageViewPreview.setImageURI(it)
                val imagePart = createMultipartFromUri(it)
                viewModel.images = listOf(imagePart)
                binding.imageViewPreview.visibility = View.VISIBLE
            }
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


        // Button click to add image
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
            if (selectedCategory == "Select category") {
                Toast.makeText(requireContext(), "Please select a category", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (selectedServiceType == "Select service type") {
                Toast.makeText(requireContext(), "Please select a service type", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            viewModel.description = binding.editTextDescription.text.toString()
            viewModel.price = binding.edPrice.text.toString()
            viewModel.address = binding.editTextAddress.text.toString()
            viewModel.time = binding.edtServiceTime.text.toString()
            viewModel.slot_time = binding.editTextSlotTime.text.toString()
            val intent = Intent(requireContext(), AddServiceDetails::class.java)
            startActivity(intent)
        }
        binding.btnAlreadyAdded.setOnClickListener {
            val intent = Intent(requireContext(), ServiceList::class.java)
            startActivity(intent)
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
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
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
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.date.setText(selectedDate)
                viewModel.date =selectedDate
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
