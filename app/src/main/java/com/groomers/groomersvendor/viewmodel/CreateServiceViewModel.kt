package com.groomers.groomersvendor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.groomers.groomersvendor.model.modelcreateservice.ModelCreateServiceX
import com.groomers.groomersvendor.model.modelupdateservice.ModelUpdateService

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
    var start_time: String? = null
    var end_time: String? = null
    var quantity: Int? = null
    var day1: Int? = null
    var day2: Int? = null
    var day3: Int? = null
    var day4: Int? = null
    var day5: Int? = null
    var day6: Int? = null
    var day7: Int? = null
    var serviceDuration: String? = null
    var imageUrl: String? = null
    var editFlag: String? = null
    var images: MultipartBody.Part? = null

     val _modelCreateService = MutableLiveData<ModelCreateServiceX?>()
     val _modelUpdateService = MutableLiveData<ModelUpdateService?>()
    val modelCreateService: LiveData<ModelCreateServiceX?> = _modelCreateService
    val modelUpdateService: LiveData<ModelUpdateService?> = _modelUpdateService

    val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    val _isLoading = MutableLiveData<Boolean>()
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
        images: MultipartBody.Part,
        servideType: String,
        discount: String, start_time : String,
        end_time : String,
        quantity  : String,
        Day1 : String,
        Day2 : String,
        Day3 : String,
        Day4 : String,
        Day5 : String,
        Day6 : String,
        Day7: String
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = apiService.createServicePost(
                    "Bearer $token",
                    serviceName,
                    description,
                    price,
                    time,
                    servideType,
                    date,
                    category,
                    address,
                    user_type,
                    images,
                    discount,
                    start_time,
                    end_time,
                    quantity.toInt() ,
                    Day1 ,
                    Day2,
                    Day3,
                    Day4 ,
                    Day5 ,
                    Day6 ,
                    Day7
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
        discount: String,
        userType: String,
        startTime: String,
        endTime: String,
        quantity: Int,
        day1: Int,
        day2: Int,
        day3: Int,
        day4: Int,
        day5: Int,
        day6: Int,
        day7: Int,
        image: MultipartBody.Part,
        id: String,
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = apiService.updateServicePost(
                    authorization = "Bearer $token",
                    id = id,
                    serviceName = serviceName,
                    description = description,
                    price = price,
                    time = time,
                    serviceType = serviceType,
                    date = date,
                    discount = discount,
                    user_type = userType,
                    startTime = startTime,
                    endTime = endTime,
                    quantity = quantity,
                    day1 = day1,
                    day2 = day2,
                    day3 = day3,
                    day4 = day4,
                    day5 = day5,
                    day6 = day6,
                    day7 = day7,
                    image = image
                )

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        if (body.status == 1) {
                            _modelUpdateService.postValue(body)
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

    fun clearServiceData() {
        _modelCreateService.value = null
        _modelUpdateService.value = null
        _errorMessage.value = null
    }
}