package com.groomers.groomersvendor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.groomers.groomersvendor.model.modelslotlist.ModelSlotList
import com.groomers.groomersvendor.retrofit.ApiService
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
@HiltViewModel
class SlotListViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
    private val sessionManager: SessionManager
) : AndroidViewModel(application) {

    private val _modelSlotList = MutableLiveData<ModelSlotList?>()
    val modelSlotList: LiveData<ModelSlotList?> = _modelSlotList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSlotList(day : String) {
//        val token = sessionManager.accessToken
        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2dyb29tZXJzLmNvLmluL2FwaS9sb2dpbiIsImlhdCI6MTczOTMwMTI2MSwiZXhwIjoxNzQwNTk3MjYxLCJuYmYiOjE3MzkzMDEyNjEsImp0aSI6InpyOHZJczZTbGM1eUVmV3YiLCJzdWIiOiIyIiwicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.2fGKO7wprOxav7f2i2lpOT1TTlQUOI-ikjApNaiNkgU"
        if (token.isNullOrEmpty()) {
            _errorMessage.postValue("Authentication error: Please log in again.")
            return
        }

        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = apiService.getTimeSlot("Bearer $token", day)

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()
                    if (responseBody?.status == 1) {
                        _modelSlotList.postValue(responseBody)
                    } else {
                        _errorMessage.postValue("No available slots. Please check again later.")
                    }
                } else {
                    _errorMessage.postValue(parseErrorMessage(response.code(), response.errorBody()?.string()))
                }
            } catch (e: IOException) {
                _errorMessage.postValue("No internet connection. Please check your network and try again.")
            } catch (e: HttpException) {
                _errorMessage.postValue("Server error (${e.code()}): Unable to process your request. Please try again later.")
            } catch (e: Exception) {
                _errorMessage.postValue("An unexpected error occurred. Please try again.")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun parseErrorMessage(statusCode: Int, errorBody: String?): String {
        return when (statusCode) {
            400 -> "Invalid request. Please check your input."
            401 -> "Session expired. Please log in again."
            403 -> "Access denied. Contact support if you believe this is an error."
            404 -> "Requested data not found. Please try again later."
            500 -> "Our servers are currently experiencing issues. Please try again later."
            else -> errorBody ?: "An error occurred. Please try again."
        }
    }
}
