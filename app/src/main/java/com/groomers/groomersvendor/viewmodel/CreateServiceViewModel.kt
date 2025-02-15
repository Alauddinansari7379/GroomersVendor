package com.groomers.groomersvendor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.groomers.groomersvendor.model.modelcreateservice.ModelCreateServiceX
import com.groomers.groomersvendor.retrofit.ApiService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okio.IOException
import retrofit2.HttpException

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

    var images: MultipartBody.Part? = null

    private val _modelCreateService = MutableLiveData<ModelCreateServiceX>()
    val modelCreateService: LiveData<ModelCreateServiceX?> = _modelCreateService

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun createService(
        token: String,
        apiService: ApiService,
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
        images: MultipartBody.Part
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = apiService.createServicePost(
                    "Bearer $token",
                    serviceName,
                    description,
                    price,
                    time="34",
                    serviceType="dkdk kd",
                    date,
                    category,
                    slot_time="34",
                    address,
                    user_type,
                    images
                )

                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.status == 1) {
                            _modelCreateService.postValue(it)
                        } else {
                            _errorMessage.postValue("Service creation failed. Please try again.")
                        }
                    } ?: _errorMessage.postValue("Unexpected response from server.")
                } else {
                    _errorMessage.postValue("Error ${response.code()}: ${response.message()}")
                }
            } catch (e: IOException) {
                _errorMessage.postValue("Network error. Please check your internet connection.")
            } catch (e: HttpException) {
                _errorMessage.postValue("Server error: ${e.message()}")
            } catch (e: Exception) {
                _errorMessage.postValue("Unexpected error: ${e.localizedMessage}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun updateService(
        token: String,
        apiService: ApiService,
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
        image: MultipartBody.Part,
        id : String
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = apiService.updateServicePost(
                    "Bearer $token",
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
                    image,
                    id
                )

                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.status == 1) {
                            _modelCreateService.postValue(it)
                        } else {
                            _errorMessage.postValue("Service update failed. Please try again.")
                        }
                    } ?: _errorMessage.postValue("Unexpected response from server.")
                } else {
                    _errorMessage.postValue("Error ${response.code()}: ${response.message()}")
                }
            } catch (e: IOException) {
                _errorMessage.postValue("Network error. Please check your internet connection.")
            } catch (e: HttpException) {
                _errorMessage.postValue("Server error: ${e.message()}")
            } catch (e: Exception) {
                _errorMessage.postValue("Unexpected error: ${e.localizedMessage}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

}