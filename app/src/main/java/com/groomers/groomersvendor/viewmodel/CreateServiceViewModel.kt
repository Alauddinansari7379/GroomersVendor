package com.groomers.groomersvendor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.groomers.groomersvendor.model.modelcreateservice.ModelCreateService
import com.groomers.groomersvendor.retrofit.ApiService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class CreateServiceViewModel(
    application: Application
) : AndroidViewModel(application) {
    var serviceName: String? = null
    var description: String? = null
    var price: String? = null
    var time: String? = null
    var serviceType: String? = null
    var date: String? = null
    var category: String? = null
    var slot_time: String? = null
    var address: String? = null
    var user_type: String? = null
    var discount: String? = null
    var serviceDuration: String? = null
    var imageUrl: String? = null
    var editFlag: String? = null

    var images: List<MultipartBody.Part>? = null

    private val _modelCreateService = MutableLiveData<ModelCreateService?>()
    val modelCreateService: LiveData<ModelCreateService?> = _modelCreateService

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun createService(token : String, apiService: ApiService,
        serviceName: String,
        description: String,
        price: String,
        time: String,
        serviceType: String,
        date: String,
        category: String,
        slot_time: String,
        address: String,
        user_type: String,
        images: List<MultipartBody.Part>
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = apiService.createServicePost("Bearer$token",
                    serviceName,
                    description,
                    price,
                    time,
                    serviceType,
                    date,
                    category,
                    slot_time,
                    address,
                    user_type,
                    images
                )
                _isLoading.postValue(false) // Hide loading state

                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        _modelCreateService.postValue(response.body())
                    } else {
                        _errorMessage.postValue("Error: ${response.message()}")
                        _isLoading.postValue(false)

                    }
                } else {
                    _errorMessage.postValue("Error: ${response.message()}")
                    _isLoading.postValue(false)
                }
            } catch (e: Exception) {
                _isLoading.postValue(false)
                _errorMessage.postValue("Exception: ${e.localizedMessage}")
            } finally {
                _isLoading.postValue(false)
            }
        }

    }


    fun updateService(token : String, apiService: ApiService,
        serviceName: String,
        description: String,
        price: String,
        time: String,
        serviceType: String,
        date: String,
        category: String,
        slot_time: String,
        address: String,
        user_type: String,
        images: List<MultipartBody.Part>
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = apiService.updateServicePost("Bearer$token",
                    serviceName,
                    description,
                    price,
                    time,
                    serviceType,
                    date,
                    category,
                    slot_time,
                    address,
                    user_type,
                    images
                )
                _isLoading.postValue(false) // Hide loading state

                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        _modelCreateService.postValue(response.body())
                    } else {
                        _errorMessage.postValue("Error: ${response.message()}")
                        _isLoading.postValue(false)

                    }
                } else {
                    _errorMessage.postValue("Error: ${response.message()}")
                    _isLoading.postValue(false)
                }
            } catch (e: Exception) {
                _isLoading.postValue(false)
                _errorMessage.postValue("Exception: ${e.localizedMessage}")
            } finally {
                _isLoading.postValue(false)
            }
        }

    }
}