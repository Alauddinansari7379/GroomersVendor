package com.groomers.groomersvendor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.groomers.groomersvendor.model.modeldeleteservice.ModelDeleteService
import com.groomers.groomersvendor.model.modelservice.ModelService
import com.groomers.groomersvendor.model.modelsingleservice.ModelSingleService
import com.groomers.groomersvendor.retrofit.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ServiceViewModel@Inject constructor(
    private val apiService: ApiService,
    application: Application
) : AndroidViewModel(application)  {
    private val _modelService = MutableLiveData<ModelService>()
    val modelService: LiveData<ModelService?> = _modelService

    private val _modelDeleteService = MutableLiveData<ModelDeleteService>()
    val modelDeleteService: LiveData<ModelDeleteService?> = _modelDeleteService

    private val _modelSingleService = MutableLiveData<ModelSingleService>()
    val modelSingleService: LiveData<ModelSingleService?> = _modelSingleService

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getServiceList(accessToken: String) {
        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = apiService.getServiceList("Bearer $accessToken")

                if (response.isSuccessful && response.body() != null) {
                    if (response.body()?.status ==1) {
                        _modelService.postValue(response.body())
                    }else{
                        _errorMessage.postValue("Error: ${response.message()}")
                        _isLoading.postValue(false)

                    }
                } else {
                    _errorMessage.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error: ${e.message}")
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    fun deleteService(accessToken: String,id : String) {
        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = apiService.deleteService("Bearer $accessToken",id)

                if (response.isSuccessful && response.body() != null) {
                    if (response.body()?.status ==1) {
                        _modelDeleteService.postValue(response.body())
                        _errorMessage.postValue(response.message())
                    }else{
                        _errorMessage.postValue("Error: ${response.message()}")
                        _isLoading.postValue(false)

                    }
                } else {
                    _errorMessage.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error: ${e.message}")
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getSingleService(accessToken: String,id : String) {
        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = apiService.getSingleService("Bearer $accessToken",id)

                if (response.isSuccessful && response.body() != null) {
                    if (response.body()?.status ==1) {
                        _modelSingleService.postValue(response.body())
                        _errorMessage.postValue(response.message())
                    }else{
                        _errorMessage.postValue("Error: ${response.message()}")
                        _isLoading.postValue(false)

                    }
                } else {
                    _errorMessage.postValue("Error: ${response.message()}")
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