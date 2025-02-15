package com.groomers.groomersvendor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.groomers.groomersvendor.model.modelcity.ModelCity
import com.groomers.groomersvendor.model.modelstate.ModelState
import com.groomers.groomersvendor.retrofit.ApiService
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val _modelCity = MutableLiveData<ModelCity>()
    val modelCity: LiveData<ModelCity> = _modelCity

    private val _modelState = MutableLiveData<ModelState>()
    val modelState: LiveData<ModelState> = _modelState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getCity(apiService: ApiService) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = apiService.getCity()
                _isLoading.postValue(false) // Hide loading state

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!
                    if (responseBody.status == 1) {
                        _modelCity.postValue(responseBody)
                    } else {
                        _errorMessage.postValue("Error: ${response.message()}")
                    }
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

    fun getState(apiService: ApiService) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = apiService.getState()
                _isLoading.postValue(false) // Hide loading state

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!
                    if (responseBody.status == 1) {
                        _modelState.postValue(responseBody)
                    } else {
                        _errorMessage.postValue("Error: ${response.message()}")
                    }
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
