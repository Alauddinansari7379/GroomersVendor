package com.groomers.groomersvendor.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.ehcf.Helper.myToast
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.ServiceList
import com.groomers.groomersvendor.adapter.AdapterServices
import com.groomers.groomersvendor.adapter.AdapterServices.Companion.serviceId
import com.groomers.groomersvendor.adapter.AdapterServices.Companion.serviceName
import com.groomers.groomersvendor.adapter.DaysAdapter
import com.groomers.groomersvendor.adapter.OthersCategoryAdapter
import com.groomers.groomersvendor.databinding.FragmentAddPostBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.helper.Toastic
import com.groomers.groomersvendor.helper.UploadRequestBody
import com.groomers.groomersvendor.model.ModelDay
import com.groomers.groomersvendor.model.modelcategory.Result
import com.groomers.groomersvendor.retrofit.ApiServiceProvider
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.groomers.groomersvendor.viewmodel.CategoryViewModel
import com.groomers.groomersvendor.viewmodel.MyApplication
import com.groomers.groomersvendor.viewmodel.ServiceViewModel
import com.groomers.groomersvendor.viewmodel.SlotViewModel
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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class AddPostFragment() : Fragment(R.layout.fragment_add_post) {
    private lateinit var serviceDuration: String
    val selectedDays = linkedSetOf<ModelDay>()
    private lateinit var endTimeFormatted: String
    private lateinit var startTimeFormatted: String
    private lateinit var discount: String
    private lateinit var serviceNameNew: String
    private var categoryList: List<Result> = emptyList()
    private lateinit var binding: FragmentAddPostBinding

    @Inject
    lateinit var sessionManager: SessionManager
    var selectedDate = ""
    var userType: String = ""
    var mydilaog: Dialog? = null
    private var selectedImageUri: Uri? = null
    private val viewModelService: ServiceViewModel by viewModels()
    private val viewModel by lazy {
        (requireActivity().application as MyApplication).createServiceViewModel
    }
    lateinit var parts: MultipartBody.Part
    private val slotViewModel: SlotViewModel by viewModels()
    lateinit var daysAdapter: DaysAdapter

    private var imageUri: Uri? = null
    var quantity = 1
    var dayList = ArrayList<ModelDay>()
    private var endTime = "00:00:00"
    private var startTime = "00:00:00"
    private var selecteddurationTime = ""
    var dayId = ""
    var day1 = ""
    var day2 = ""
    var day3 = ""
    var day4 = ""
    var day5 = ""
    var day6 = ""
    var day7 = ""
    private lateinit var adapterServices: AdapterServices


    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                binding.imageViewPreview.setImageURI(it)
                handleImageSelection(it)
                binding.imageViewPreview.visibility = View.VISIBLE
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = FragmentAddPostBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    private val categoryViewModel: CategoryViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterServices = AdapterServices(emptyList(), requireContext())
        val postId = arguments?.getString("postId")
        setupSpinners()
        setupSpinners1()
        observeViewModel1()
        setupClickListeners()
        Log.e("BankName", sessionManager.bankName.toString())

        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        dayList.add(ModelDay("Monday", "1"))
        dayList.add(ModelDay("Tuesday", "2"))
        dayList.add(ModelDay("Wednesday", "3"))
        dayList.add(ModelDay("Thursday", "4"))
        dayList.add(ModelDay("Friday", "5"))
        dayList.add(ModelDay("Saturday", "6"))
        dayList.add(ModelDay("Sunday", "7"))

        binding.spinnerDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                if (view == null) {
                    return
                }

                if (dayList.isNotEmpty()) {
                    val modelDay = dayList[i]
                    dayId = modelDay.id.toString()
                    when (dayId) {
                        "1" -> {
                            day1 = "1"
                            viewModel.day1 = day1.toInt()
                        }

                        "2" -> {
                            day2 = "2"
                            viewModel.day2 = day2.toInt()
                        }

                        "3" -> {
                            day3 = "3"
                            viewModel.day3 = day3.toInt()
                        }

                        "4" -> {
                            day4 = "4"
                            viewModel.day4 = day4.toInt()
                        }

                        "5" -> {
                            day5 = "5"
                            viewModel.day5 = day5.toInt()
                        }

                        "6" -> {
                            day6 = "6"
                            viewModel.day6 = day6.toInt()
                        }

                        "7" -> {
                            day7 = "7"
                            viewModel.day7 = day7.toInt()
                        }

                    }

                    // Check if it's already selected
                    if (selectedDays.any { it.id == modelDay.id }) {
                        Toast.makeText(
                            requireContext(),
                            "${modelDay.day} is already selected",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        selectedDays.add(modelDay)
                        daysAdapter.updateList(selectedDays.toMutableList())
                    }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        }



        setDaySpinner()
        binding.tvDate.text = currentDate

        mydilaog?.setCanceledOnTouchOutside(false)
        mydilaog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val newCalendar1 = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                DateFormat.getDateInstance().format(newDate.time)
                val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(newDate.time)
                binding.tvDate.text = date

                selectedDate = SimpleDateFormat(
                    "E MMMM dd,yyyy hh:mm a",
                    Locale.getDefault()
                ).format(newDate.time)
                Log.e("selectedDate", selectedDate)
            },
            newCalendar1[Calendar.YEAR],
            newCalendar1[Calendar.MONTH],
            newCalendar1[Calendar.DAY_OF_MONTH]
        )
        //datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.datePicker.minDate = System.currentTimeMillis()


        binding.btnDate.setOnClickListener {
            datePicker.show()
        }

        binding.btnStartTime.setOnClickListener {
            startTime()
        }
        binding.btnEndTime.setOnClickListener {
            endTime()
        }

        binding.btnPlus.setOnClickListener {
            quantity++
            binding.tvQuantity.text = quantity.toString()
        }

        binding.btnMinus.setOnClickListener {
            if (quantity > 1) { // Prevents negative values
                quantity--
                binding.tvQuantity.text = quantity.toString()
            }
        }
        categoryViewModel.getCategory(ApiServiceProvider.getApiService())




        binding.layoutGallery.setOnClickListener { pickImageLauncher.launch("image/*") }
//        binding.layoutCamera.setOnClickListener {
//            imageUri = createImageUri() // This may return null
//
//            imageUri?.let {
//                takePictureLauncher.launch(it)
//            } ?: showError("Unable to create image URI")
//        }

        binding.layoutCamera.setOnClickListener {
            checkCameraPermissionAndLaunch()
        }

        binding.date.setOnClickListener { openDatePickerDialog() }

        binding.btnAddPost.setOnClickListener { validateAndProceed() }

        binding.btnAlreadyAdded.setOnClickListener {
            startActivity(Intent(requireContext(), ServiceList::class.java))
        }
        // Observe error message if login fails
        categoryViewModel.errorMessage.observe(requireActivity()) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        categoryViewModel.modelCategory.observe(requireActivity()) { modelCategory ->
            binding.rvService.apply {
                if (modelCategory != null) {
                    adapter = OthersCategoryAdapter(modelCategory.result, requireContext())
                }
            }
            val gridLayoutManager =
                GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
            binding.rvService.layoutManager = gridLayoutManager
            if (modelCategory != null) {
                binding.rvService.adapter = AdapterServices(modelCategory.result, requireContext())
            }
            if (modelCategory != null) {
                categoryList = modelCategory.result
            }
            if (!postId.isNullOrEmpty()) {
                viewModel.editFlag = postId
                binding.btnAddPost.text = "Update"
                sessionManager.accessToken?.let { token ->
                    lifecycleScope.launch {
                        viewModelService.getSingleService(token, postId)
                        observeViewModel()
                    }
                } ?: showError("Error: Missing Token")
            }
        }


        // Observe isLoading to show/hide progress
        viewModel.isLoading.observe(requireActivity()) { isLoading ->
            if (isLoading) {
                CustomLoader.showLoaderDialog(context)
            } else {
                CustomLoader.hideLoaderDialog()
            }
        }
        // Observe error message if login fails
        viewModel.errorMessage.observe(requireActivity()) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Observe the result of the login attempt
        slotViewModel.modelSlot.observe(requireActivity()) { modelSlot ->
            if (modelSlot != null && modelSlot.status == 1) {

                Toastic.toastic(
                    context = requireContext(),
                    message = "Service added successfully.",
                    duration = Toastic.LENGTH_SHORT,
                    type = Toastic.SUCCESS,
                    isIconAnimated = true,
                    textColor = Color.BLUE,
                ).show()
                resetAllFields()
            } else {
                if (modelSlot != null) {
                    Toastic.toastic(
                        context = requireContext(),
                        message = modelSlot.message,
                        duration = Toastic.LENGTH_SHORT,
                        type = Toastic.ERROR,
                        isIconAnimated = true,
                        textColor = Color.BLUE,
                    ).show()
                }
            }
        }
    }

    private fun createImageUri(): Uri? {
        return try {
            val file = File(requireContext().cacheDir, "captured_image.jpg")
            if (!file.parentFile.exists()) file.parentFile.mkdirs()

            FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                file
            )
        } catch (e: Exception) {
            Log.e("ImageUriError", "Failed to create URI: ${e.message}", e)
            null
        }
    }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera() // Proceed to launch camera
            } else {
                showError("Camera permission denied")
            }
        }

    private fun checkCameraPermissionAndLaunch() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted
                openCamera()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // Show rationale or dialog if needed
                AlertDialog.Builder(requireContext())
                    .setTitle("Permission Required")
                    .setMessage("Camera access is required to take photos.")
                    .setPositiveButton("OK") { _, _ ->
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }

            else -> {
                // Directly ask for permission
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        imageUri = createImageUri()
        imageUri?.let {
            takePictureLauncher.launch(it)
        } ?: showError("Unable to create image URI")
    }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri?.let {
                    binding.imageViewPreview.setImageURI(it)
                    binding.imageViewPreview.visibility = View.VISIBLE
                    handleImageSelection1(it)
                }
            } else {
                showError("Failed to capture image")
            }
        }


    private fun validateAndProceed() {
        viewModel.serviceName = serviceName
        startTimeFormatted = binding.tvStartTime.text.toString()
        endTimeFormatted = binding.tvEndTime.text.toString()
        userType = binding.spinnerUserType.selectedItem.toString()
        discount = binding.etDiscount.text.toString().trim()
        serviceDuration = ""
        serviceDuration = binding.etDuration.text.toString().trim()
        serviceDuration = convertIntoMin(serviceDuration)
        serviceNameNew = binding.etServiceName.text.toString()
        // Step 1: Validate Service Name
        if (viewModel.serviceName.isNullOrEmpty()) {
            showError1("Please select a service before proceeding!")
            return
        }
        if (serviceName.isEmpty()) {
            showErrorField1(binding.etServiceName, "Please enter Service name")
            return
        }
        // Step 2: Validate Description
        viewModel.description = binding.editTextDescription.text.toString().trim()
        if (viewModel.description.isNullOrEmpty()) {
            showErrorField1(binding.editTextDescription, "Please enter a description")
            return
        }

        // Step 3: Validate Service Image
        if (viewModel.images == null) {
            showError1("Please select a service image")
            return
        }

        // Step 4: Validate Price
        viewModel.price = binding.edPrice.text.toString().trim()
        if (viewModel.price.isNullOrEmpty()) {
            showErrorField1(binding.edPrice, "Please enter a price")
            return
        }

        // Step 5: Validate Date
        viewModel.date = binding.date.text.toString().trim()
        if (viewModel.date.isNullOrEmpty()) {
            showErrorField1(binding.date, "Please enter a service date")
            return
        }

        // Step 6: Validate User Type
        if (userType.isEmpty()) {
            showError1("Please select a user type")
            return
        }

        // Step 7: Validate Discount
//        if (discount.isEmpty()) {
//            showErrorField1(binding.etDiscount, "Please enter discount")
//            return
//        }

        // Step 9: Validate Start Time
        if (startTimeFormatted.contentEquals("00:00:00")) {
            showWarningDialog("The start time must be greater 00:00:00")
            return
        }

        // Step 10: Validate End Time
        if (endTimeFormatted.contentEquals("00:00:00")) {
            showWarningDialog("The End time must be greater 00:00:00")
            return
        }
        if (startTimeFormatted > endTimeFormatted) {
            Toast.makeText(
                context,
                "Start time should not be greater than End time",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        // Step 8: Validate Service Duration
        if (serviceDuration.isEmpty()) {
            showErrorField1(binding.etDuration, "Please enter service duration")
            return
        } else {
            viewModel.time = serviceDuration
        }

        sessionManager.accessToken?.let { token ->
            viewModel.images?.let { imageList ->
                if (!viewModel.editFlag.isNullOrEmpty()) {


                    viewModel.updateService(
                        token = token,
                        apiService = ApiServiceProvider.getApiService(),
                        serviceName = serviceNameNew,
                        description = viewModel.description ?: "",
                        price = viewModel.price ?: "",
                        time = viewModel.time ?: "",
                        serviceType = viewModel.serviceType ?: "",
                        date = viewModel.date ?: "",
                        discount = discount,
                        userType = userType,
                        startTime = viewModel.start_time
                            ?: "00:00:00",  // Add this field in your ViewModel
                        endTime = viewModel.end_time ?: "01:00:00",      // Add this too
                        quantity = viewModel.quantity ?: 1,
                        day1 = viewModel.day1 ?: 1,
                        day2 = viewModel.day2 ?: 2,
                        day3 = viewModel.day3 ?: 3,
                        day4 = viewModel.day4 ?: 4,
                        day5 = viewModel.day5 ?: 5,
                        day6 = viewModel.day6 ?: 6,
                        day7 = viewModel.day7 ?: 7,
                        image = imageList, // Safely access first image or return
                        id = viewModel.editFlag ?: "" // editFlag used as ID
                    )

                } else {
                    viewModel.createService(
                        token,
                        ApiServiceProvider.getApiService(),
                        serviceNameNew,
                        viewModel.description ?: "",
                        viewModel.price ?: "",
                        viewModel.time ?: "",
                        viewModel.serviceType ?: "",
                        viewModel.date ?: "",
                        viewModel.category ?: "",
                        viewModel.slot_time ?: "",
                        viewModel.address ?: "",
                        userType,
                        imageList,
                        serviceId,
                        discount,
                        startTime,
                        endTime,
                        quantity.toString(),
                        day1,
                        day2,
                        day3,
                        day4,
                        day5,
                        day6,
                        day7
                    )
                }
            }
        }

    }

    private fun convertIntoMin(serviceDuration: String): String {
        // Split the time into hours and minutes
        var totalMinutes = ""
        val timeParts = serviceDuration.split(":")
        if (timeParts.size == 2) {
            val hours = timeParts[0].toIntOrNull() ?: 0 // Convert the hour part to an integer
            val minutes = timeParts[1].toIntOrNull() ?: 0 // Convert the minute part to an integer

            // Convert to total minutes
            totalMinutes = ((hours * 60) + minutes).toString()
            // Use totalMinutes as needed
            Log.d("Time Conversion", "Total minutes: $totalMinutes")
        } else {
            // Handle invalid time format
            Log.e("Time Conversion", "Invalid time format: $serviceDuration")
        }
        return totalMinutes
    }

    // Helper method to show warning dialogs
    private fun showWarningDialog(message: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText(message)
            .setConfirmText("Ok")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog -> sDialog.cancel() }
            .setCancelClickListener { sDialog -> sDialog.cancel() }
            .show()
    }

    // Helper method to show field-specific errors
    private fun showErrorField1(view: EditText, message: String) {
        view.error = message
        view.requestFocus()
    }

    // Helper method to show generic errors
    private fun showError1(message: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
            .setTitleText(message)
            .setConfirmText("Ok")
            .show()
    }


    private fun handleImageSelection(uri: Uri) {
        createMultipartFromUri(requireContext(), uri)?.let {
            parts = it
            viewModel.images = parts
        }
    }

    private fun handleImageSelection1(uri: Uri) {
        createMultipartFromUri1(requireContext(), uri)?.let {
            parts = it
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
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]

        )
        datePickerDialog.datePicker.minDate =
            System.currentTimeMillis() - 1000 // Disable past dates
        datePickerDialog.show()
    }

    private fun observeViewModel() {
        if (isAdded) {
            viewModelService.isLoading.observe(requireActivity()) { isLoading ->
                if (isLoading) CustomLoader.showLoaderDialog(requireContext())
                else CustomLoader.hideLoaderDialog()
            }

            viewModelService.errorMessage.observe(requireActivity()) { errorMessage ->
                showError(errorMessage)
            }
        }

        if (isAdded) {
            viewModelService.modelSingleService.observe(requireActivity()) { response ->
                response?.result?.firstOrNull()?.let { service ->
                    viewModel.apply {
                        //                    category = service.category.toString() ?: ""
                        serviceType = service.serviceType ?: ""
                        description = service.description ?: ""
                        price = service.price?.toString() ?: ""
                        //                    address = service.address.toString() ?: ""
                        time = service.time ?: ""
                        date = service.date ?: ""
                        //                    slot_time = service.slot_time.toString() ?: ""
                        serviceName = service.serviceName ?: ""
                        user_type = service.user_type ?: ""
                        imageUrl = service.image
                        discount = service.discount
                        val result = convertMinutesToHourMinFormat(service.time)
                        time = result
                        start_time = service.start_time
                        end_time = service.end_time
                        quantity = service.quantity
                        day1 = service.Day1
                        day2 = service.Day2
                        day3 = service.Day3
                        day4 = service.Day4
                        day5 = service.Day5
                        day6 = service.Day6
                        day7 = service.Day7
                        user_type = service.user_type


                        // Mapping numbers to day names
                        val dayMap = mapOf(
                            "1" to "Monday",
                            "2" to "Tuesday",
                            "3" to "Wednesday",
                            "4" to "Thursday",
                            "5" to "Friday",
                            "6" to "Saturday",
                            "7" to "Sunday"
                        )

                        // Combine all day strings
                        val allDaysCombined = listOf(
                            service.Day1,
                            service.Day2,
                            service.Day3,
                            service.Day4,
                            service.Day5,
                            service.Day6,
                            service.Day7
                        )
                            .filterNotNull() // Ignore nulls if any
                            .joinToString(",") // Merge into one comma-separated string

                        // Split, remove duplicates, trim spaces
                        val uniqueDayIds = allDaysCombined.split(",").map { it.trim() }.toSet()

                        selectedDays.clear()
                        // Build the selectedDays list
                        for (id in uniqueDayIds) {
                            dayMap[id]?.let { dayName ->
                                selectedDays.add(
                                    ModelDay(
                                        day = dayName,
                                        id = id,
                                        isSelected = true
                                    )
                                )
                            }
                        }

                        daysAdapter.updateList(selectedDays.toMutableList())

                    }
                    binding.editTextDescription.setText(viewModel.description)
                    binding.etServiceName.setText(viewModel.serviceName)
                    binding.edPrice.setText(viewModel.price)
                    binding.date.setText(viewModel.date)
                    binding.etDiscount.setText(viewModel.discount)
                    binding.etDuration.setText(viewModel.time)
                    binding.editTextAddress.setText(viewModel.address)
                    binding.tvStartTime.text = viewModel.start_time
                    binding.tvEndTime.text = viewModel.end_time
                    binding.tvQuantity.text = viewModel.quantity.toString()
                    adapterServices!!.updateData(categoryList, viewModel.serviceType.toString())
                    AdapterServices(categoryList, requireContext())
                    binding.rvService.adapter = adapterServices
                    adapterServices!!.selectedItemById(viewModel.serviceType!!)

                    var selectedIndex = 0
                    selectedIndex = if (viewModel.user_type!!.contains("Male")) {
                        0
                    } else if (viewModel.user_type!!.contains("Female")) {
                        1
                    } else {
                        2
                    }
                    if (selectedIndex != -1) {
                        binding.spinnerUserType.setSelection(selectedIndex)
                    }

                    val imageUrl = "${ApiServiceProvider.IMAGE_URL}${viewModel.imageUrl}"

                    Glide.with(requireContext())
                        .load(imageUrl)
                        .into(binding.imageViewPreview)

                    // Download the image and convert it to MultipartBody
                    downloadImageAndConvertToMultipart(imageUrl)

                }

            }
        }
    }

    private fun convertMinutesToHourMinFormat(minutesStr: String): String {
        val totalMinutes = minutesStr.toIntOrNull() ?: return "00:00"
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return String.format("%02d:%02d", hours, minutes)
    }


    private fun showError(message: String) {
        if (message != null) {
            myToast(requireContext(),message,false)
        }
    }


    private fun ContentResolver.getFileName(uri: Uri): String {
        return query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex != -1) cursor.getString(nameIndex) else "temp_image.jpg"
        } ?: "temp_image.jpg"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding
        viewModel.editFlag = ""
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
                    val multipartBody =
                        MultipartBody.Part.createFormData("image", file.name, requestBody)

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

    private fun setupSpinners() {
        val serviceList = listOf(
            "Select service", "Haircut & Styling", "Beard Trimming & Shaping",
            "Hair Coloring", "Head Massage", "Hair Spa", "Dandruff Treatment",
            "Shaving & Beard Styling", "Facial & Cleanup", "Face Bleach",
            "Anti-Tan Treatment", "Acne Treatment"
        )

        val serviceAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, serviceList)
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerService.adapter = serviceAdapter
    }

    @SuppressLint("SetTextI18n", "LogNotTimber", "DefaultLocale")
    private fun startTime() {
        val view = layoutInflater.inflate(R.layout.time_picker_dialog, null)
        val dialog = Dialog(requireContext())

        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view)
        }

        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()

        val btnOkTimePicker = view.findViewById<Button>(R.id.btnOkTimePicker)
        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        val tvTimeTimePicker = view.findViewById<TextView>(R.id.tvTimeTimePicker)

        timePicker.setIs24HourView(true) // Set to 24-hour format

        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val formattedHour = String.format("%02d", hourOfDay)
            val formattedMinute = String.format("%02d", minute)
            val selectedTime = "$formattedHour:$formattedMinute:00" // Ensure "HH:mm:ss" format

            tvTimeTimePicker.text = selectedTime
            binding.tvStartTime.text = selectedTime
            startTime = selectedTime
            viewModel.start_time = startTime

            Log.e("Selected Start Time", startTime)
        }

        btnOkTimePicker.setOnClickListener {
            dialog.dismiss()
        }
    }

    @SuppressLint("SetTextI18n", "LogNotTimber", "DefaultLocale")
    private fun durationTime() {
        val view = layoutInflater.inflate(R.layout.time_picker_dialog, null)
        val dialog = Dialog(requireContext())

        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view)
        }

        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()

        val btnOkTimePicker = view.findViewById<Button>(R.id.btnOkTimePicker)
        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)


        timePicker.setIs24HourView(true) // Set to 24-hour format

        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
            binding.etDuration.setText(formattedTime)
            selecteddurationTime = formattedTime



            Log.e("Selected Duration Time", selecteddurationTime)
        }

        btnOkTimePicker.setOnClickListener {
            dialog.dismiss()
        }
    }

    @SuppressLint("SetTextI18n", "LogNotTimber")
    private fun endTime() {
        val view = layoutInflater.inflate(R.layout.time_picker_dialog, null)
        val dialog = Dialog(requireContext())

        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view)
        }

        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.show()

        val btnOkTimePicker = view.findViewById<Button>(R.id.btnOkTimePicker)
        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        val tvTimeTimePicker = view.findViewById<TextView>(R.id.tvTimeTimePicker)

        timePicker.setIs24HourView(true) // Set to 24-hour format

        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val formattedHour = String.format("%02d", hourOfDay)
            val formattedMinute = String.format("%02d", minute)
            val selectedTime = "$formattedHour:$formattedMinute:00" // Ensure "HH:mm:ss" format

            tvTimeTimePicker.text = selectedTime
            binding.tvEndTime.text = selectedTime
            endTime = selectedTime
            viewModel.end_time = endTime

            Log.e("Selected End Time", endTime)
        }

        btnOkTimePicker.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun setupSpinners1() {
        val userTypeList = listOf("Male", "Female", "Pet")

        // Use requireContext() instead of 'this'
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, userTypeList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerUserType.adapter = adapter

        // Set selected user type
        val selectedIndex = userTypeList.indexOf(viewModel.user_type)
        if (selectedIndex != -1) {
            binding.spinnerUserType.setSelection(selectedIndex)
        }
    }

    private fun setupClickListeners() {
        binding.etDuration.setOnClickListener {
            durationTime()
        }

    }

    private fun observeViewModel1() {
        if (isAdded) {
            viewModel.modelCreateService.observe(requireActivity()) { result ->
                if (result != null && result.status == 1) {
                    for (i in selectedDays) {

                        if (serviceDuration.isNotEmpty()) {
                            slotViewModel.createSlot(
                                ApiServiceProvider.getApiService(),
                                startTimeFormatted,
                                endTimeFormatted,
                                i.id,
                                serviceId,
                                quantity.toString(),
                                result.result.id.toString(), serviceDuration
                            )
                        }
                        Log.i("Slot created", serviceDuration)
                    }
                }

            }

            viewModel.modelUpdateService.observe(requireActivity()) { result ->
                if (result != null && result.status == 1) {
                    Toastic.toastic(
                        context = requireContext(),
                        message = "Service updated successfully.",
                        duration = Toastic.LENGTH_SHORT,
                        type = Toastic.SUCCESS,
                        isIconAnimated = true,
                        textColor = Color.BLUE,
                    ).show()
                    requireActivity().finish()
                }
            }



            viewModel.isLoading.observe(requireActivity()) { isLoading ->
                if (isLoading) {
                    CustomLoader.showLoaderDialog(requireContext())
                } else {
                    CustomLoader.hideLoaderDialog()

                }
            }

            viewModel.errorMessage.observe(requireActivity()) { errorMessage ->
                if (errorMessage != null) {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Function to reset all fields
    @RequiresApi(Build.VERSION_CODES.M)
    private fun resetAllFields() {
        setupSpinners()
        setupSpinners1()

        serviceName = ""
        binding.editTextDescription.text?.clear()
        binding.imageViewPreview.setImageDrawable(Color.TRANSPARENT.toDrawable())
        serviceDuration = ""
        binding.edPrice.text.clear()
        binding.date.text.clear()
        binding.etDiscount.text.clear()
        binding.etDuration.text?.clear()
        binding.etServiceName.text?.clear()
        binding.tvStartTime.text = "00:00:00"
        binding.tvEndTime.text = "00:00:00"
        selectedDays.clear()
        daysAdapter.updateList(selectedDays.toMutableList())
        binding.tvQuantity.text = "1"
        binding.layoutDateFromDilog.visibility = View.GONE
        binding.btnCreate.visibility = View.GONE
        categoryViewModel.getCategory(ApiServiceProvider.getApiService())
        setDaySpinner()

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearServiceData()
        viewModel.modelCreateService.removeObservers(this)
        viewModel.isLoading.removeObservers(this)
        viewModel.errorMessage.removeObservers(this)
        categoryViewModel.clearServiceData()
        slotViewModel.clearData()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setDaySpinner() {
        // Initialize the adapter with selectedDays
        daysAdapter = DaysAdapter(
            requireContext(),
            selectedDays.toMutableList()
        ) { removedDay ->
            selectedDays.remove(removedDay) // Remove from list
            daysAdapter.updateList(selectedDays.toMutableList()) // Update RecyclerView
        }

        binding.rvHorizontalList.adapter = daysAdapter
        binding.spinnerDay.adapter =
            ArrayAdapter<ModelDay>(requireContext(), android.R.layout.simple_list_item_1, dayList)

        // Set up the Select All / Clear All toggle functionality
        binding.tvSelectAllDays.setOnClickListener {
            if (binding.tvSelectAllDays.text == "Select All") {
                // Select all days
                selectedDays.clear()
                selectedDays.addAll(dayList)  // Assuming dayList contains all available days
                daysAdapter.updateList(selectedDays.toMutableList()) // Update RecyclerView
                day1 = "1"
                day2 = "2"
                day3 = "3"
                day4 = "4"
                day5 = "5"
                day6 = "6"
                day7 = "7"
                // Change the button text to "Clear All" and set the color
                binding.tvSelectAllDays.text = "Clear All"
                binding.tvSelectAllDays.setTextColor(
                    resources.getColor(
                        android.R.color.holo_red_dark,
                        null
                    )
                ) // Red color for "Clear All"
            } else {
                // Clear all selections but keep one day selected
                val firstDay =
                    dayList.firstOrNull() // Get the first day or any default day you want selected
                selectedDays.clear()
                if (firstDay != null) {
                    selectedDays.add(firstDay)  // Select only the first day
                }
                daysAdapter.updateList(selectedDays.toMutableList()) // Update RecyclerView
                day1 = ""
                day2 = ""
                day3 = ""
                day4 = ""
                day5 = ""
                day6 = ""
                day7 = ""
                viewModel.day1 = null
                viewModel.day2 = null
                viewModel.day3 = null
                viewModel.day4 = null
                viewModel.day5 = null
                viewModel.day6 = null
                viewModel.day7 = null

                // Change the button text back to "Select All" and set the color
                binding.tvSelectAllDays.text = "Select All"
                binding.tvSelectAllDays.setTextColor(
                    resources.getColor(
                        android.R.color.holo_green_dark,
                        null
                    )
                ) // Green color for "Select All"
            }
        }
    }


}
