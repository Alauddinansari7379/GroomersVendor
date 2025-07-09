package com.groomers.groomersvendor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.groomers.model.modelvendorrating.ModelVendorRating
import com.groomers.groomersvendor.model.rating.Rating
import com.groomers.groomersvendor.retrofit.ApiService
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    application: Application,
    private val apiService: ApiService,
) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)

    private val _modelRating = MutableLiveData<Rating?>()
    val modelRating: LiveData<Rating?> = _modelRating

    private val _modelVendorRating = MutableLiveData<ModelVendorRating?>()
    val modelVendorRating: LiveData<ModelVendorRating?> = _modelVendorRating

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun customerRating(
        bookingId: String,
        rating: String,
        comments: String,
    ) {
        _isLoading.postValue(true)

        val token = sessionManager.accessToken.orEmpty()

        viewModelScope.launch {
            try {
                val response = apiService.customerRating(
                    "Bearer $token",
                    bookingId,
                    rating,
                    comments
                )

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.status == 1) {
                        _modelRating.postValue(responseBody)
                    } else {
                        _errorMessage.postValue(responseBody?.message ?: "Rating submission failed.")
                    }
                } else {
                    _errorMessage.postValue("Server error: ${response.code()} - ${response.message()}")
                }

            } catch (e: IOException) {
                _errorMessage.postValue("No internet connection. Please check your network.")
            } catch (e: HttpException) {
                _errorMessage.postValue("Server error. Please try again later.")
            } catch (e: Exception) {
                _errorMessage.postValue("Unexpected error occurred.")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getAllVendorRatings(
        vendorId: String,
    ) {
        _isLoading.postValue(true)

        val token = sessionManager.accessToken.orEmpty()

        viewModelScope.launch {
            try {
                val response = apiService.getAllVendorRatings(
                    "Bearer $token",
                    vendorId,
                )

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.status == 1) {
                        _modelVendorRating.postValue(responseBody)
                    } else {
                        _errorMessage.postValue(
                            responseBody?.message ?: "Rating submission failed."
                        )
                    }
                } else {
                    _errorMessage.postValue("Server error: ${response.code()} - ${response.message()}")
                }

            } catch (e: IOException) {
                _errorMessage.postValue("No internet connection. Please check your network.")
            } catch (e: HttpException) {
                _errorMessage.postValue("Server error. Please try again later.")
            } catch (e: Exception) {
                _errorMessage.postValue("Unexpected error occurred.")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun clearData() {
        _modelRating.value = null
        _errorMessage.value = null
        _modelVendorRating.value = null
    }
}
