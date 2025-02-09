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

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val _modelCity = MutableLiveData<ModelCity>()
    val modelCity : LiveData<ModelCity> = _modelCity

    private val _modelState = MutableLiveData<ModelState>()
    val modelState : LiveData<ModelState> = _modelState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun getCity(apiService: ApiService){
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = apiService.getCity()
                if(response.isSuccessful && response.body()!= null){
                    val responseBody = response.body()!!
                    if (responseBody.status == 1){
                        _modelCity.postValue(responseBody)
                    }else{
                        _errorMessage.postValue("Something went wrong!")
                    }
                }
            } catch (e: Exception) {
                _isLoading.postValue(false)
                _errorMessage.postValue("Something went wrong!")
            }finally {
                _isLoading.postValue(false)
            }
        }
    }
    fun getState(apiService: ApiService){
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = apiService.getState()
                if(response.isSuccessful && response.body()!= null){
                    val responseBody = response.body()!!
                    if (responseBody.status == 1){
                        _modelState.postValue(responseBody)
                    }else{
                        _errorMessage.postValue("Something went wrong!")
                    }
                }
            } catch (e: Exception) {
                _isLoading.postValue(false)
                _errorMessage.postValue("Something went wrong!")
            }finally {
                _isLoading.postValue(false)
            }
        }
    }
}