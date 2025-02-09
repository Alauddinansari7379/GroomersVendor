package com.groomers.groomersvendor.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.AddServiceDetails
import com.groomers.groomersvendor.activity.ServiceList
import java.util.Calendar


class AddPostFragment : Fragment(R.layout.fragment_add_post) {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var btnAddImage: TextView
    private lateinit var imageViewPreview: ImageView
    private lateinit var spinnerServiceType: Spinner
    private lateinit var spinnerCategory: Spinner
    private lateinit var editTextSlotTime: EditText
    private lateinit var addPost: Button
    private lateinit var alreadyAdded: Button


    private var selectedImageUri: Uri? = null

    // Define image picker activity result launcher
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            imageViewPreview.setImageURI(it)
            imageViewPreview.visibility = View.VISIBLE
        }
    }

    // Example lists for Service Type and Category
    private val serviceTypeList = listOf("Cleaning", "Delivery", "Repair", "Maintenance")
    private val categoryList = listOf("Household", "Electronics", "Furniture", "Apparel")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        editTextTitle = view.findViewById(R.id.editTextTitle)
        editTextDescription = view.findViewById(R.id.editTextDescription)
        btnAddImage = view.findViewById(R.id.btnAddImage)
        imageViewPreview = view.findViewById(R.id.imageViewPreview)
        spinnerServiceType = view.findViewById(R.id.spinnerServiceType)
        spinnerCategory = view.findViewById(R.id.spinnerCategory)
        editTextSlotTime = view.findViewById(R.id.editTextSlotTime)
        addPost = view.findViewById(R.id.btnAddPost)
        alreadyAdded = view.findViewById(R.id.btnAlreadyAdded)

        // Button click to add image
        btnAddImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Set up spinners with lists
        setupSpinners()

        // Set up calendar selection for slot time
        editTextSlotTime.setOnClickListener {
            openDatePickerDialog()
        }
        addPost.setOnClickListener {
            val intent = Intent(requireContext(), AddServiceDetails::class.java)
            startActivity(intent)
        }
        alreadyAdded.setOnClickListener {
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
        spinnerServiceType.adapter = serviceTypeAdapter

        // Set up the adapter for Category spinner using the categoryList
        val categoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categoryList
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter
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
                editTextSlotTime.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}
