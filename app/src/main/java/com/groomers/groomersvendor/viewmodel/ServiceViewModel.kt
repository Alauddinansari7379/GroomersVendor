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
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application
) : AndroidViewModel(application) {

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
                _isLoading.postValue(false)

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        if (body.status == 1) {
                            _modelService.postValue(body)
                        } else {
                            _errorMessage.postValue(body.message ?: "Failed to fetch services. Please try again.")
                        }
                    } ?: run {
                        _errorMessage.postValue("Unexpected response from the server.")
                    }
                } else {
                    _errorMessage.postValue("Failed to load services. Please try again later.")
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

    fun deleteService(accessToken: String, id: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = apiService.deleteService("Bearer $accessToken", id)
                _isLoading.postValue(false)

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        if (body.status == 1) {
                            _modelDeleteService.postValue(body)
                            _errorMessage.postValue("Service deleted successfully.")
                        } else {
                            _errorMessage.postValue(body.message ?: "Failed to delete service.")
                        }
                    } ?: run {
                        _errorMessage.postValue("Unexpected response from the server.")
                    }
                } else {
                    _errorMessage.postValue("Failed to delete service. Please try again.")
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

    fun getSingleService(accessToken: String, id: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = apiService.getSingleService("Bearer $accessToken", id)
                _isLoading.postValue(false)

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        if (body.status == 1) {
                            _modelSingleService.postValue(body)
                        } else {
                            _errorMessage.postValue(body.message ?: "Service details not found.")
                        }
                    } ?: run {
                        _errorMessage.postValue("Unexpected response from the server.")
                    }
                } else {
                    _errorMessage.postValue("Failed to load service details. Please try again.")
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
