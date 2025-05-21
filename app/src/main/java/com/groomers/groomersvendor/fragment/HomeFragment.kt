package com.groomers.groomersvendor.fragment

import CalendarAdapter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.adapter.AdapterBooking
import com.groomers.groomersvendor.databinding.FragmentHomeBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.helper.Toastic
import com.groomers.groomersvendor.model.modelGetBooking.Result
import com.groomers.groomersvendor.retrofit.ApiServiceProvider
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.groomers.groomersvendor.viewmodel.AcceptBookingViewModel
import com.groomers.groomersvendor.viewmodel.GetBookingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), AdapterBooking.Accept {


    lateinit var binding: FragmentHomeBinding
    private val calendar = Calendar.getInstance()
    private val bookingViewModel: GetBookingViewModel by viewModels()
    private val acceptBookingViewModel: AcceptBookingViewModel by viewModels()
    private lateinit var mainData: ArrayList<Result>

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
        Log.e("AccessToken", sessionManager.accessToken.toString())

        // Get the background color of the fragment's root view
        val backgroundColor = (view.background as? ColorDrawable)?.color ?: Color.WHITE

        // Update the status bar color to match the background color
        (activity as? Common)?.updateStatusBarColor(backgroundColor)
        makeGetBookingAPICall(getCurrentDate())
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

    private fun makeGetBookingAPICall(date: String) {
        val apiService = ApiServiceProvider.getApiService()
        sessionManager.accessToken?.let { token ->
            lifecycleScope.launch {
                bookingViewModel.getBooking(apiService, token, date)
            }
        } ?: run {
            Toastic.toastic(
                context = requireActivity(),
                message = "Missing Token.",
                duration = Toastic.LENGTH_SHORT,
                type = Toastic.ERROR,
                isIconAnimated = true,
                textColor = if (false) Color.BLUE else null,
            ).show()
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
            mainData = modelCategory.result
            setRecyclerViewAdapter(mainData)

        }
        binding.searchView.addTextChangedListener { str ->
            if (::mainData.isInitialized) {
                val filtered = mainData.filter {
                    it.customerName != null && it.customerName!!.contains(str.toString(), ignoreCase = true)
                }
                setRecyclerViewAdapter(ArrayList(filtered))
            }
        }

    }
    private fun setRecyclerViewAdapter(data: ArrayList<Result>) {
        binding.rvOrderList.apply {
            adapter = AdapterBooking(data, requireContext(), this@HomeFragment)
        }

        if (data.isEmpty()) {
            binding.tvNoDataFound.visibility = View.VISIBLE
            binding.rvOrderList.visibility = View.GONE
        } else {
            binding.rvOrderList.visibility = View.VISIBLE
            binding.tvNoDataFound.visibility = View.GONE
        }
    }

    private fun makeAcceptBookingAPICall(bookingId: String, slug: String) {
        val apiService = ApiServiceProvider.getApiService()
        sessionManager.accessToken?.let { token ->
            lifecycleScope.launch {
                acceptBookingViewModel.acceptBooking(apiService, token, bookingId, slug)
            }
        } ?: run {
            Toastic.toastic(
                context = requireActivity(),
                message = "Missing Token.",
                duration = Toastic.LENGTH_SHORT,
                type = Toastic.ERROR,
                isIconAnimated = true,
                textColor = if (false) Color.BLUE else null,
            ).show()
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
            Toastic.toastic(
                context = requireActivity(),
                message = response.message,
                duration = Toastic.LENGTH_SHORT,
                type = Toastic.SUCCESS,
                isIconAnimated = true,
                textColor = if (false) Color.BLUE else null,
            ).show()
            makeGetBookingAPICall("")
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
        binding.tvYear.text = sdfY.format(calendar.time) // Update Year title

        val dates = getDatesForMonth(calendar) // Fetch dates for the selected month
        val today = Calendar.getInstance() // Get today's date

        // Create adapter with a click listener
        val adapter = CalendarAdapter(dates, today) { selectedDate ->

            makeGetBookingAPICall(selectedDate)
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

    override fun onResume() {
        super.onResume()
        makeGetBookingAPICall(getCurrentDate())

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

    override fun reject(bookingId: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to Reject?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                makeAcceptBookingAPICall(bookingId, "rejected")
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }

    override fun complete(bookingId: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to Complete?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                makeAcceptBookingAPICall(bookingId, "completed")
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}
