package com.groomers.groomersvendor.fragment

import CalendarAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.adapter.AdapterBooking
import com.groomers.groomersvendor.databinding.FragmentHomeBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.retrofit.ApiServiceProvider
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.groomers.groomersvendor.viewmodel.AcceptBookingViewModel
import com.groomers.groomersvendor.viewmodel.GetBookingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), AdapterBooking.Accept {
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val REQUEST_CODE = 100
    private lateinit var currentLocation: TextView

    lateinit var binding: FragmentHomeBinding
    private val calendar = Calendar.getInstance()
    private val bookingViewModel: GetBookingViewModel by viewModels()
    private val acceptBookingViewModel: AcceptBookingViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        if (sessionManager.imageUrl.isNullOrEmpty()) {
            sessionManager.imageUrl = "https://groomers.co.in/public/uploads/"
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        getLastLocation()
        // Get the background color of the fragment's root view
        val backgroundColor = (view.background as? ColorDrawable)?.color ?: Color.WHITE

        // Update the status bar color to match the background color
        (activity as? Common)?.updateStatusBarColor(backgroundColor)
        makeGetBookingAPICall()
        with(binding) {

            updateCalendar()

            imgArrUp.setOnClickListener {
                calendar.add(Calendar.MONTH, 1)
                updateCalendar()
            }

            imgArrBack.setOnClickListener {
                calendar.add(Calendar.MONTH, -1)
                updateCalendar()
            }
        }


    }

    private fun makeGetBookingAPICall() {
        val apiService = ApiServiceProvider.getApiService()
        sessionManager.accessToken?.let { token ->
            lifecycleScope.launch {
                bookingViewModel.getBooking(apiService, token)
            }
        } ?: run {
            Toast.makeText(requireContext(), "Error: Missing Token", Toast.LENGTH_LONG).show()
        }
        // Observe isLoading to show/hide progress
        bookingViewModel.isLoading.observe(requireActivity()) { isLoading ->
            if (isLoading) {
                CustomLoader.showLoaderDialog(context)
            } else {
                CustomLoader.hideLoaderDialog()
            }
        }
        // Observe error message if login fails
        bookingViewModel.errorMessage.observe(requireActivity()) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        bookingViewModel.modelGetBooking.observe(requireActivity()) { modelCategory ->
            binding.rvOrderList.apply {
                adapter = AdapterBooking(modelCategory.result, requireContext(), this@HomeFragment)
            }

        }
    }

    private fun makeAcceptBookingAPICall(bookingId: String, slug: String) {
        val apiService = ApiServiceProvider.getApiService()
        sessionManager.accessToken?.let { token ->
            lifecycleScope.launch {
                acceptBookingViewModel.acceptBooking(apiService, token, bookingId, slug)
            }
        } ?: run {
            Toast.makeText(requireContext(), "Error: Missing Token", Toast.LENGTH_LONG).show()
        }
        // Observe isLoading to show/hide progress
        acceptBookingViewModel.isLoading.observe(requireActivity()) { isLoading ->
            if (isLoading) {
                CustomLoader.showLoaderDialog(context)
            } else {
                CustomLoader.hideLoaderDialog()
            }
        }
        // Observe error message if login fails
        acceptBookingViewModel.errorMessage.observe(requireActivity()) { response ->
            if (response.isNotEmpty()) {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show()
            }
        }
        acceptBookingViewModel.modelAccept.observe(requireActivity()) { response ->
            Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
            makeGetBookingAPICall()
        }
    }

    private fun getDatesForMonth(calendar: Calendar): List<Date> {
        val dates = mutableListOf<Date>()
        val tempCalendar = calendar.clone() as Calendar
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1)

        val maxDays = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 0 until maxDays) {
            dates.add(tempCalendar.time)
            tempCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return dates
    }

    private fun updateCalendar() {
        val sdf = SimpleDateFormat("MMMM", Locale.getDefault())
        val sdfY = SimpleDateFormat("yyyy", Locale.getDefault())
        binding.tvMonth.text = sdf.format(calendar.time) // Update month title
        binding.tvYear.text = sdfY.format(calendar.time) // Update month title

        val dates = getDatesForMonth(calendar) // Fetch dates for the selected month
        val today = Calendar.getInstance() // Get today's date

        // Create adapter with a click listener
        val adapter = CalendarAdapter(dates, today) { selectedDate ->
            Toast.makeText(requireContext(), "Selected Date: $selectedDate", Toast.LENGTH_SHORT)
                .show()
        }
        binding.recyclerView.adapter = adapter

        // Set up a horizontal LinearLayoutManager
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = layoutManager

        // Scroll to the current date in the center
        val todayPosition = adapter.getTodayPosition()
        binding.recyclerView.post {
            layoutManager.scrollToPositionWithOffset(todayPosition, binding.recyclerView.width / 2)
        }
    }


    @SuppressLint("SetTextI18n", "LogNotTimber")
    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient!!.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null && isAdded) {
                        try {
                            val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                            val addresses =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            Log.e(
                                ContentValues.TAG,
                                "addresses[0].latitude: ${addresses?.get(0)?.latitude}"
                            )
                            Log.e(
                                ContentValues.TAG,
                                "addresses[0].longitude: ${addresses?.get(0)?.longitude}"
                            )

                            addresses?.get(0)?.getAddressLine(0)

                            val locality = addresses?.get(0)?.locality
                            val countryName = addresses?.get(0)?.countryName
                            val countryCode = addresses?.get(0)?.countryCode
                            val postalCode = addresses?.get(0)?.postalCode.toString()
                            val subLocality = addresses?.get(0)?.subLocality
                            val subAdminArea = addresses?.get(0)?.subAdminArea
                            // currentAddress = "$subLocality, $locality, $countryName"
                            postalCodeNew = postalCode

                            // Update location text
                            binding.tvLocation.text = addresses?.get(0)?.getAddressLine(0)

                            Log.e(ContentValues.TAG, "locality: $locality")
                            Log.e(ContentValues.TAG, "countryName: $countryName")
                            Log.e(ContentValues.TAG, "countryCode: $countryCode")
                            Log.e(ContentValues.TAG, "postalCode: $postalCode")
                            Log.e(ContentValues.TAG, "subLocality: $subLocality")
                            Log.e(ContentValues.TAG, "subAdminArea: $subAdminArea")
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
        } else {
            askPermission()
        }
    }


    companion object {
        var postalCodeNew = ""
    }

    private fun askPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }

    override fun accept(bookingId: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to Accept?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                makeAcceptBookingAPICall(bookingId, "accepted")
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()

    }
}
