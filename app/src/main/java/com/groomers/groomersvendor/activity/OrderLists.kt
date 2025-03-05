package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.groomers.groomersvendor.adapter.DeleteService
import com.groomers.groomersvendor.adapter.ServiceAdapter
import com.groomers.groomersvendor.databinding.ActivityOrderListsBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.groomers.groomersvendor.viewmodel.ServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class OrderLists : AppCompatActivity(),DeleteService {
    private val binding by lazy { ActivityOrderListsBinding.inflate(layoutInflater) }
    private val viewModel: ServiceViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    private lateinit var serviceAdapter: ServiceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.llmain.setOnClickListener {
            startActivity(Intent(this@OrderLists, OrderDetail::class.java))
        }

        setupRecyclerView()
        observeViewModel()
        sessionManager.accessToken?.let { token ->
            lifecycleScope.launch {
                viewModel.getServiceList(token)
            }
        } ?: run {
            Toast.makeText(this, "Error: Missing Token", Toast.LENGTH_LONG).show()
        }

    }
    private fun setupRecyclerView() {
        serviceAdapter = ServiceAdapter(this,emptyList(), this)
        binding.rvService.adapter = serviceAdapter
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) CustomLoader.showLoaderDialog(this@OrderLists)
            else CustomLoader.hideLoaderDialog()
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.modelService.observe(this) { response ->
            response?.result?.let { services ->
                binding.rvService.apply {
                    adapter = ServiceAdapter(this@OrderLists,services, this@OrderLists)
                }
            } ?: run {
                Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.modelDeleteService.observe(this) { isDeleted ->
            if (isDeleted!!.status == 1) {
                Toast.makeText(this, "Service deleted successfully", Toast.LENGTH_SHORT).show()
                sessionManager.accessToken?.let { token ->
                    lifecycleScope.launch {
                        viewModel.getServiceList(token) // Refresh list after deletion
                    }
                }
            }
        }
    }

    override fun deleteService(id: String) {
        TODO("Not yet implemented")
    }
}