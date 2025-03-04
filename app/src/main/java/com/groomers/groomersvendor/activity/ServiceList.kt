package com.groomers.groomersvendor.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.groomers.groomersvendor.adapter.DeleteService
import com.groomers.groomersvendor.adapter.ServiceAdapter
import com.groomers.groomersvendor.databinding.ActivityServiceListBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.groomers.groomersvendor.viewmodel.ServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ServiceList : AppCompatActivity(), DeleteService {
    private val viewModel: ServiceViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    private val binding by lazy { ActivityServiceListBinding.inflate(layoutInflater) }
    private lateinit var serviceAdapter: ServiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()

        sessionManager.accessToken?.let { token ->
            lifecycleScope.launch {
                viewModel.getServiceList(token)
            }
        } ?: run {
            Toast.makeText(this, "Error: Missing Token", Toast.LENGTH_LONG).show()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        serviceAdapter = ServiceAdapter(this,emptyList(), this)
        binding.rvService.adapter = serviceAdapter
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) CustomLoader.showLoaderDialog(this@ServiceList)
            else CustomLoader.hideLoaderDialog()
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.modelService.observe(this) { response ->
            response?.result?.let { services ->
                binding.rvService.apply {
                    adapter = ServiceAdapter(this@ServiceList,services, this@ServiceList)
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
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to Delete?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                sessionManager.accessToken?.let { token ->
                    lifecycleScope.launch {
                        viewModel.deleteService(token, id)
                    }
                } ?: run {
                    Toast.makeText(this, "Error: Missing Token", Toast.LENGTH_LONG).show()
                }
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()


    }
}
