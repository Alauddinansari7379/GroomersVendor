package com.groomers.groomersvendor.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityManageSlotsBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.helper.Toastic
import com.groomers.groomersvendor.model.ModelDay
import com.groomers.groomersvendor.retrofit.ApiServiceProvider
import com.groomers.groomersvendor.viewmodel.SlotViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ManageSlots : AppCompatActivity() {
    private val context = this@ManageSlots
    private lateinit var binding: ActivityManageSlotsBinding
    var mydilaog: Dialog? = null
    var datePicker: Dialog? = null
    var dialog: Dialog? = null
    var selectedDate = ""
    var dayId = ""
    private val viewModel: SlotViewModel by viewModels()

    private var startTime = "00:00:00"
    var dayList = ArrayList<ModelDay>()

    var quantity = 1
    private var endTime = "00:00:00"

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageSlotsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinners()
        var view = layoutInflater.inflate(R.layout.time_picker_dialog, null)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())



        dayList.add(ModelDay("Monday", "1"))
        dayList.add(ModelDay("Tuesday", "2"))
        dayList.add(ModelDay("Wednesday", "3"))
        dayList.add(ModelDay("Thursday", "4"))
        dayList.add(ModelDay("Friday", "5"))
        dayList.add(ModelDay("Saturday", "6"))
        dayList.add(ModelDay("Sunday", "7"))











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







        val apiService = ApiServiceProvider.getApiService()


        // Observe isLoading to show/hide progress
        viewModel.isLoading.observe(context) { isLoading ->
            if (isLoading) {
                CustomLoader.showLoaderDialog(context)
            } else {
                CustomLoader.hideLoaderDialog()
            }
        }
        // Observe error message if login fails
        viewModel.errorMessage.observe(context) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        // Observe the result of the login attempt
        viewModel.modelSlot.observe(context) { modelSlot ->
            if (modelSlot!=null && modelSlot.status == 1) {
                Toastic.toastic(
                    context = this@ManageSlots,
                    message = "Slot create successfully",
                    duration = Toastic.LENGTH_SHORT,
                    type = Toastic.SUCCESS,
                    isIconAnimated = true,
                    textColor = if (false) Color.BLUE else null,
                ).show()
                finish()
            }
        }

        binding.spinnerDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (dayList.size > 0) {
                    dayId = dayList[i].id.toString()
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        binding.spinnerDay.adapter =
            ArrayAdapter<ModelDay>(context, android.R.layout.simple_list_item_1, dayList)


        binding.tvDate.text = currentDate

        mydilaog?.setCanceledOnTouchOutside(false)
        mydilaog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val newCalendar1 = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
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







        binding.btnCreate.setOnClickListener {
            val endT = binding.tvStartTime.text.toString().replace(":", "").toString()
            val selectedService = binding.spinnerService.selectedItem.toString()

            if (endT == "000000"
            ) {
                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("The session slabs must be within the time limit of the clinic timings")
                    .setConfirmText("Ok")
                    .showCancelButton(true)
                    .setConfirmClickListener { sDialog ->
                        sDialog.cancel()

                    }
                    .setCancelClickListener { sDialog ->
                        sDialog.cancel()
                    }
                    .show()
                return@setOnClickListener
            }
            if (selectedService == "Select service") {
                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Please select the service")
                    .setConfirmText("Ok")
                    .showCancelButton(true)
                    .setConfirmClickListener { sDialog ->
                        sDialog.cancel()

                    }
                    .setCancelClickListener { sDialog ->
                        sDialog.cancel()
                    }
                    .show()
                return@setOnClickListener
            }



            val endT1 = binding.tvEndTime.text.toString().replace(":", "").toString()
            if (endT1 == "000000"
            ) {
                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("The session slabs must be within the time limit of the clinic timings")
                    .setConfirmText("Ok")
                    .showCancelButton(true)
                    .setConfirmClickListener { sDialog ->
                        sDialog.cancel()
                    }
                    .setCancelClickListener { sDialog ->
                        sDialog.cancel()
                    }
                    .show()
                return@setOnClickListener

            } else {

                viewModel.createSlot(apiService, startTime, endTime,dayId,selectedService,quantity.toString(),"")
            }
        }
    }


    @SuppressLint("SetTextI18n", "LogNotTimber", "DefaultLocale")
    private fun startTime() {
        val view = layoutInflater.inflate(R.layout.time_picker_dialog, null)
        val dialog = Dialog(this)

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

            Log.e("Selected Start Time", startTime)
        }

        btnOkTimePicker.setOnClickListener {
            dialog.dismiss()
        }
    }
    @SuppressLint("SetTextI18n", "LogNotTimber")
    private fun endTime() {
        val view = layoutInflater.inflate(R.layout.time_picker_dialog, null)
        val dialog = Dialog(this)

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

            Log.e("Selected End Time", endTime)
        }

        btnOkTimePicker.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun setupSpinners() {
        val serviceList = listOf("Select service","Haircut & Styling"," Beard Trimming & Shaping","Hair Coloring","Head Massage","Hair Spa","Dandruff Treatment","Shaving & Beard Styling","Facial & Cleanup","Face Bleach","Anti-Tan Treatment","Acne Treatment")
        val serviceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, serviceList)
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerService.adapter = serviceAdapter

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearData()
    }
}