package com.groomers.groomersvendor.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.ehcf.Helper.currency
import com.example.ehcf.Helper.myToast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.BankInformation
import com.groomers.groomersvendor.adapter.AdapterFinance
import com.groomers.groomersvendor.databinding.FragmentFinanceBinding
import com.groomers.groomersvendor.helper.AppProgressBar
import com.groomers.groomersvendor.model.modelEarning.ModelEarning
import com.groomers.groomersvendor.retrofit.ApiClient
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class FinanceFragment : Fragment() {
    private lateinit var binding: FragmentFinanceBinding
    var count = 0
    var todayRevenueData: List<Int> = emptyList()
    var weekRevenueData: List<Int> = emptyList()
    var motnthlyRevenueData: List<Int> = emptyList()


    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiCallFinanceData()
        setupChart(binding.lineChart, "today") // Load Today's Data by Default
        setupSpinners1()
        with(binding) {
            if (sessionManager.isBank == "1") {
                setupNowButton.visibility = View.GONE
            }
            setupNowButton.setOnClickListener {
                val intent = Intent(requireContext(), BankInformation::class.java)
                intent.putExtra("IS_FROM_FINANCE", true)
                startActivity(intent)
            }
            tabWeek.setBackgroundResource(R.drawable.tab_unselected)

            // Show Weekly Revenue Data
//            tabWeek.setOnClickListener {
//                tabWeek.setBackgroundResource(R.drawable.tab_selected)
//                tabToday.setBackgroundResource(R.drawable.tab_unselected)
//                tabMonth.setBackgroundResource(R.drawable.tab_unselected)
//                setupChart(binding.lineChart, "week")
//            }


            // Show Monthly Revenue Data
//            tabMonth.setOnClickListener {
//                tabMonth.setBackgroundResource(R.drawable.tab_selected)
//                tabWeek.setBackgroundResource(R.drawable.tab_unselected)
//                tabToday.setBackgroundResource(R.drawable.tab_unselected)
//                setupChart(binding.lineChart, "month")
//            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (sessionManager.isBank == "1") {
            binding.setupNowButton.visibility = View.GONE
        }
    }

    private fun setupChart(lineChart: LineChart, type: String) {
        val entries = mutableListOf<Entry>()

        // Assign revenue data based on the selected type
        weekRevenueData = when (type) {
            "today" -> motnthlyRevenueData.toMutableList()
            "week" -> weekRevenueData
            "month" ->motnthlyRevenueData
            else -> listOf(0).toMutableList()
        }

        // Populate entries based on the selected data type
        for (i in weekRevenueData.indices) {
            entries.add(Entry(i.toFloat(), weekRevenueData[i].toFloat()))
        }

        val dataSet = LineDataSet(
            entries, when (type) {
                "today" -> "Today's Revenue"
                "week" -> "Weekly Revenue"
                "month" -> "Monthly Revenue"
                else -> "Revenue"
            }
        ).apply {
            color = Color.GREEN
            valueTextColor = Color.BLACK
            lineWidth = 2f
            setDrawCircles(false)
            setDrawValues(false)
        }

        lineChart.data = LineData(dataSet)

        // Customize Chart
        lineChart.apply {
            description.isEnabled = false
            legend.form = Legend.LegendForm.LINE
            setPinchZoom(true)
            setScaleEnabled(true)
            invalidate() // Refresh Chart
        }

        // Customize X-Axis
        lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textColor = Color.BLACK
            setDrawGridLines(false)
            granularity = 1f
            labelCount = if (type == "today") 6 else if (type == "week") 7 else 10
        }

        // Customize Y-Axis
        lineChart.axisLeft.apply {
            textColor = Color.BLACK
            setDrawGridLines(true)
        }

        lineChart.axisRight.isEnabled = false
    }

    private fun setupSpinners1() {
        val userTypeList = listOf("Actions", "Today", "Week", "Month")
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, userTypeList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerAction.adapter = adapter

        // Set listener for spinner selection
        binding.spinnerAction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (userTypeList[position]) {
                    "Today" -> {
                        setupChart(binding.lineChart, "today")
                        selectTab(binding.tabToday)
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.lineChart.visibility = View.GONE
                    }

                    "Week" -> {
                        setupChart(binding.lineChart, "week")
                        selectTab(binding.tabWeek)
                        binding.recyclerView.visibility = View.GONE
                        binding.lineChart.visibility = View.VISIBLE
                        binding.tvNoDataFound.visibility = View.GONE

                    }

                    "Month" -> {
                        setupChart(binding.lineChart, "month")
                        selectTab(binding.tabMonth)
                        binding.recyclerView.visibility = View.GONE
                        binding.lineChart.visibility = View.VISIBLE
                        binding.tvNoDataFound.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun apiCallFinanceData() {
        ApiClient.apiService.vendorEarning(
            "Bearer ${sessionManager.accessToken}",
        )
            .enqueue(object : Callback<ModelEarning> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelEarning>, response: Response<ModelEarning>
                ) {
                    try {
                        AppProgressBar.hideLoaderDialog()
                        when (response.code()) {
                            404 -> myToast(requireContext(), "Something went wrong", false)
                            500 -> myToast(requireContext(), "Server Error", false)
                            else -> {
                                if (response.body()?.result!!.earnings.isNotEmpty()) {
                                    response.body()?.result!!.earnings?.let { earningsList ->
                                        binding.recyclerView.apply {
                                            adapter = AdapterFinance(requireContext(), earningsList)
                                        }
                                        binding.tvNoDataFound.visibility = View.GONE
                                    }

                                    response.body()?.result!!.today_order.let { earningsList ->
                                        todayRevenueData = earningsList.map { it.amount.toInt() }

                                    }
                                    response.body()?.result!!.weekly_order.let { earningsList ->
                                        weekRevenueData = earningsList.map { it.amount.toInt() }

                                    }
                                    response.body()?.result!!.monthly_order.let { earningsList ->
                                        motnthlyRevenueData =
                                            earningsList.map { it.amount.toInt() } // (replace with actual monthly data if needed)

                                    }
                                } else {
                                    binding.tvNoDataFound.visibility = View.GONE
                                }
                                binding.balanceText.text =
                                    currency + response.body()!!.result.total_earnings
                                binding.availableAmountText.text =
                                    currency + response.body()!!.result.total_earnings + " available to transfer. Info"
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(requireContext(), "Something went wrong", false)
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelEarning>, t: Throwable) {
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallFinanceData()
                    } else {
                        myToast(requireContext(), t.message.toString(), false)
                        AppProgressBar.hideLoaderDialog()

                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })

    }

    // Function to highlight selected tab
    private fun selectTab(selectedTab: View) {
        with(binding) {
            tabToday.setBackgroundResource(R.drawable.tab_unselected)
            tabWeek.setBackgroundResource(R.drawable.tab_unselected)
            tabMonth.setBackgroundResource(R.drawable.tab_unselected)

            selectedTab.setBackgroundResource(R.drawable.tab_selected)
        }
    }

}
