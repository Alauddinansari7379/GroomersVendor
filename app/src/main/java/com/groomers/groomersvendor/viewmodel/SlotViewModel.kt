package com.groomers.groomersvendor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.groomers.groomersvendor.model.modelslot.ModelSlot
import com.groomers.groomersvendor.retrofit.ApiService
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import kotlinx.coroutines.launch

class SlotViewModel (application: Application) : AndroidViewModel(application) {
    private val sessionManager = SessionManager(application)

    private val _modelSlot = MutableLiveData<ModelSlot?>()
    val modelSlot: LiveData<ModelSlot?> = _modelSlot

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun createSlot(apiService: ApiService, startTime: String, endTime: String,day : String) {
        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = apiService.createSlot(startTime, endTime,day)

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()
                    if (responseBody?.status == 1) {
                        _modelSlot.postValue(responseBody)
                        _errorMessage.postValue(responseBody.message)
                    } else {
                        _errorMessage.postValue("Something went wrong!")
                    }
                } else {
                    _errorMessage.postValue("Failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error: ${e.message}")
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}