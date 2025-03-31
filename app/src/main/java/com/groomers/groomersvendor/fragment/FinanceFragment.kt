package com.groomers.groomersvendor.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.activity.BankInformation
import com.groomers.groomersvendor.databinding.FragmentFinanceBinding
import com.groomers.groomersvendor.databinding.FragmentProfileBinding
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class FinanceFragment : Fragment() {
    private lateinit var binding: FragmentFinanceBinding

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

            // Show Weekly Revenue Data
//            tabWeek.setOnClickListener {
//                tabWeek.setBackgroundResource(R.drawable.tab_selected)
//                tabToday.setBackgroundResource(R.drawable.tab_unselected)
//                tabMonth.setBackgroundResource(R.drawable.tab_unselected)
//                setupChart(binding.lineChart, "week")
//            }

            // Show Today's Revenue Data
//            tabToday.setOnClickListener {
//                tabToday.setBackgroundResource(R.drawable.tab_selected)
//                tabWeek.setBackgroundResource(R.drawable.tab_unselected)
//                tabMonth.setBackgroundResource(R.drawable.tab_unselected)
//                setupChart(binding.lineChart, "today")
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
        val revenueData: List<Int>

        // Assign revenue data based on the selected type
        revenueData = when (type) {
            "today" -> listOf(50, 75, 100, 150, 120, 200, 300, 250, 275, 400, 500, 600, 450, 480, 550, 700, 650, 720, 800, 900, 850, 950, 980, 1000)
            "week" -> listOf(5000, 7000, 6500, 8000, 9000, 7500, 10000) // 7 days revenue
            "month" -> listOf(20000, 25000, 22000, 28000, 30000, 27000, 32000, 33000, 31000, 35000, 37000, 39000, 40000, 41000, 42000, 44000, 46000, 48000, 50000, 52000, 54000, 55000, 58000, 60000, 62000, 64000, 66000, 68000, 70000, 72000) // 30 days revenue
            else -> listOf()
        }

        // Populate entries based on the selected data type
        for (i in revenueData.indices) {
            entries.add(Entry(i.toFloat(), revenueData[i].toFloat()))
        }

        val dataSet = LineDataSet(entries, when (type) {
            "today" -> "Today's Revenue"
            "week" -> "Weekly Revenue"
            "month" -> "Monthly Revenue"
            else -> "Revenue"
        }).apply {
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
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (userTypeList[position]) {
                    "Today" -> {
                        setupChart(binding.lineChart, "today")
                        selectTab(binding.tabToday)
                    }
                    "Week" -> {
                        setupChart(binding.lineChart, "week")
                        selectTab(binding.tabWeek)
                    }
                    "Month" -> {
                        setupChart(binding.lineChart, "month")
                        selectTab(binding.tabMonth)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
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
