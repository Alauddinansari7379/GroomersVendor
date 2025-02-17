package com.groomers.groomersvendor.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.adapter.ServiceAdapter
import com.groomers.groomersvendor.databinding.ActivityOrderDetailListBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.model.modelservice.ModelService
import com.groomers.groomersvendor.retrofit.ApiService
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import com.groomers.groomersvendor.viewmodel.MyApplication
import com.groomers.groomersvendor.viewmodel.ServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrderDetail : AppCompatActivity() {
    val binding by lazy { ActivityOrderDetailListBinding.inflate(layoutInflater) }
    private val viewModel: ServiceViewModel by viewModels()
    private val viewModelService: ServiceViewModel by viewModels()

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val id =intent.getStringExtra("id").toString()
        sessionManager.accessToken?.let { token ->
            lifecycleScope.launch {
                viewModelService.getSingleService(token, id)
                observeViewModel()
            }
        }

    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) CustomLoader.showLoaderDialog(this@OrderDetail)
            else CustomLoader.hideLoaderDialog()
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.modelService.observe(this) { response ->
            response?.result?.let { services ->
                with(binding){
                    date.text=response.result.get(0).date
                    tvName.text=response.result.get(0).serviceName
                    orderId.text=response.result.get(0).id.toString()
                    tvOrderPayment.text=response.result.get(0).price.toString()
                    date.text=response.result.get(0).date
                }


            } ?: run {
                Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show()
            }
        }

    }

}