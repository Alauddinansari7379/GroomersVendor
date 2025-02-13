package com.groomers.groomersvendor.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.groomers.groomersvendor.adapter.ServiceAdapter
import com.groomers.groomersvendor.databinding.ActivityServiceListBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.groomers.groomersvendor.viewmodel.ServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class ServiceList : AppCompatActivity() {
    private val viewModel: ServiceViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager

private val binding by lazy { ActivityServiceListBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.getServiceList(sessionManager.accessToken!!)
        // Observe loading state
        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                CustomLoader.showLoaderDialog(this@ServiceList)
            } else {
                CustomLoader.hideLoaderDialog()
            }
        })

        // Observe error message
        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        })

        // Observe success response
        viewModel.modelService.observe(this, Observer { response ->
            binding.rvService.apply {
                if (response != null) {
                    adapter = ServiceAdapter(response.result)
                }
            }

        })

    }
}