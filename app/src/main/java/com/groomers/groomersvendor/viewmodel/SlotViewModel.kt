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
import retrofit2.HttpException
import java.io.IOException

class SlotViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionManager = SessionManager(application)

    private val _modelSlot = MutableLiveData<ModelSlot?>()
    val modelSlot: LiveData<ModelSlot?> = _modelSlot

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun createSlot(apiService: ApiService, startTime: String, endTime: String, day:String,service : String,qty : String ) {
        _isLoading.postValue(true)
        var token = ""
        if (sessionManager.accessToken != null) {
            token = sessionManager.accessToken!!
        }
        viewModelScope.launch {
            try {
                val response = apiService.createSlot("Bearer $token", startTime, endTime, day,service,qty)
                _isLoading.postValue(false)

                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        if (responseBody.status == 1) {
                            _modelSlot.postValue(responseBody)
                        } else {
                            _errorMessage.postValue(responseBody.message ?: "Something went wrong!")
                        }
                    } ?: _errorMessage.postValue("Unexpected response from the server.")
                } else {
                    _errorMessage.postValue("Failed to create slot. Please try again.")
                }
            } catch (e: IOException) {
                _errorMessage.postValue("No internet connection. Please check your network.")
            } catch (e: HttpException) {
                _errorMessage.postValue("Server error. Please try again later.")
            } catch (e: Exception) {
                _errorMessage.postValue("Something went wrong. Please try again.")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
