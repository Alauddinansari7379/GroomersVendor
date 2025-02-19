package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.groomers.groomersvendor.MainActivity
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.adapter.AdapterSlotsList
import com.groomers.groomersvendor.databinding.ActivityMySlotBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.groomers.groomersvendor.viewmodel.LoginViewModel
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sessionManager.accessToken?.let { token ->
            lifecycleScope.launch {
                viewModel.getSlotList(token)
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
            modelSlotList?.let {
                binding.rvManageSlot.adapter = AdapterSlotsList(this@MySlot, it)
            } ?: run {
                Toast.makeText(this@MySlot, "No slots available", Toast.LENGTH_SHORT).show()
            }
        }





        // Observe error message if login fails
        viewModel.errorMessage.observe(this@MySlot) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(this@MySlot, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

    }
}