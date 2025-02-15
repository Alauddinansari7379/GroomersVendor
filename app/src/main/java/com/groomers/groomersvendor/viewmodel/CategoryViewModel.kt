package com.groomers.groomersvendor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.groomers.groomersvendor.model.modelcategory.ModelCategory
import com.groomers.groomersvendor.retrofit.ApiService
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

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

                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        if (responseBody.status == 1) {
                            _modelCategory.postValue(responseBody)
                        } else {
                            _errorMessage.postValue("Error: Invalid category data")
                        }
                    } ?: _errorMessage.postValue("Error: Empty response from server")
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.postValue(errorBody ?: "Server error: ${response.message()}")
                }
            } catch (e: IOException) {
                _errorMessage.postValue("Network error: Please check your internet connection")
            } catch (e: HttpException) {
                _errorMessage.postValue("Server error: ${e.message}")
            } catch (e: Exception) {
                _errorMessage.postValue("Unexpected error: ${e.localizedMessage}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
