package com.groomers.groomersvendor.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.groomers.groomersvendor.adapter.AdapterSlotsList
import com.groomers.groomersvendor.databinding.ActivityMySlotBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.model.ModelDay
import com.groomers.groomersvendor.model.modelslotlist.ModelSlotList
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.groomers.groomersvendor.viewmodel.SlotListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MySlot : AppCompatActivity() {
    private val binding by lazy { ActivityMySlotBinding.inflate(layoutInflater) }
    private val viewModel: SlotListViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    var dayList = ArrayList<ModelDay>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        dayList.add(ModelDay("Monday", "1"))
        dayList.add(ModelDay("Tuesday", "2"))
        dayList.add(ModelDay("Wednesday", "3"))
        dayList.add(ModelDay("Thursday", "4"))
        dayList.add(ModelDay("Friday", "5"))
        dayList.add(ModelDay("Saturday", "6"))
        dayList.add(ModelDay("Sunday", "7"))

        sessionManager.accessToken?.let { token ->
            lifecycleScope.launch {
                viewModel.getSlotList("1")
            }
        } ?: run {
            Toast.makeText(this@MySlot, "Error: Missing Token", Toast.LENGTH_LONG).show()
        }
        // Observe isLoading to show/hide progress
        viewModel.isLoading.observe(this@MySlot) { isLoading ->
            if (isLoading) {
                CustomLoader.showLoaderDialog(this@MySlot)
            } else {
                CustomLoader.hideLoaderDialog()
            }
        }

        viewModel.modelSlotList.observe(this@MySlot) { modelSlotList ->
            val safeModelSlotList = modelSlotList ?: ModelSlotList(
                message = "No slots available",
                result = emptyList(),
                status = 0
            )

            val adapter = AdapterSlotsList(this@MySlot, safeModelSlotList)
            binding.rvManageSlot.adapter = adapter
            if (safeModelSlotList.result.isEmpty()) {
                Toast.makeText(this@MySlot, "No slots available", Toast.LENGTH_SHORT).show()
            }
        }



        // Observe error message if login fails
        viewModel.errorMessage.observe(this@MySlot) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(this@MySlot, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        binding.spinnerDay.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    if (dayList.size > 0) {
                        dayId = dayList[i].id
                        day = dayList[i].day
                        index = i


                        Log.e("Dayid", dayId)
                        lifecycleScope.launch {
                            viewModel.getSlotList(dayId)
                        }
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                }
            }
        binding.spinnerDay.adapter =
            ArrayAdapter<ModelDay>(this@MySlot, android.R.layout.simple_list_item_1, dayList)
    }
    companion object {
        var day = ""
        var dayId = "0"
        var index = 0
    }
}