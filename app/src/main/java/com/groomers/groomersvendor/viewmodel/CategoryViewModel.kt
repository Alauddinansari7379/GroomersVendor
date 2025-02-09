package com.groomers.groomersvendor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.groomers.groomersvendor.model.modelcategory.ModelCategory
import com.groomers.groomersvendor.retrofit.ApiService
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val _modelCategory = MutableLiveData<ModelCategory>()
    val modelCategory: LiveData<ModelCategory> = _modelCategory

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getCategory(apiService: ApiService) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = apiService.getCategory()
                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!
                    if (responseBody.status == 1) {
                        _modelCategory.postValue(responseBody)
                    } else {
                        _errorMessage.postValue("Something went wrong!")
                    }
                }
            } catch (e: Exception) {
                _isLoading.postValue(false)
                _errorMessage.postValue("Something went wrong!")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}