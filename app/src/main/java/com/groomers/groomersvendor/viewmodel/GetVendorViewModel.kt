package com.groomers.groomersvendor.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.groomers.groomersvendor.model.ModelGetVendor.ModelGetVendor
import com.groomers.groomersvendor.model.modellogin.ModelLogin

import com.groomers.groomersvendor.retrofit.ApiService
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class GetVendorViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
    private val sessionManager: SessionManager
) : AndroidViewModel(application) {

    private lateinit var response: Response<ModelGetVendor>
    private val _modelGetVendor = MutableLiveData<ModelGetVendor?>()
    val modelGetVendor: LiveData<ModelGetVendor?> = _modelGetVendor

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getVendor() {
        _isLoading.postValue(true)
        val token = sessionManager.accessToken
        if (token.isNullOrEmpty()) {
            _errorMessage.postValue("Authentication error: Please log in again.")
            return
        }
        viewModelScope.launch {
            try {
                response= apiService.getVendor("Bearer $token")
                val responseBody = response.body()
                if (!responseBody?.result.isNullOrEmpty()) {
                    _modelGetVendor.postValue(responseBody)
                    for (i in responseBody!!.result) {
                        sessionManager.name = i.name
                        sessionManager.role = i.role
                        sessionManager.mobile = i.mobile
                        sessionManager.email = i.email
                        sessionManager.businessName = i.businessName
                        sessionManager.coverImage = i.vendorCoverImage.toString()
                        sessionManager.teamSize = i.teamSize
                        sessionManager.address = i.address
                        sessionManager.isBank = i.IsBank.toString()
                        sessionManager.accountNumber = i.AccountNumber
                        sessionManager.accountName = i.AccountName
                        sessionManager.bankName = i.BankName
                        sessionManager.ifscCode = i.ifsc_code
                        sessionManager.branchName = i.BranchName
                        sessionManager.profilePictureUrl = i.profile_picture
                        sessionManager.coverPictureUrl = i.vendorCoverImage

                    }

                }
            } catch (e: IOException) {
                // Network-related error (e.g., no internet connection)
                _errorMessage.postValue("No internet connection. Please check your network and try again.")
            } catch (e: HttpException) {
                // Handle HTTP-specific errors
                _errorMessage.postValue("Server error (${e.code()}): Unable to process your request. Please try again later.")
            } catch (e: Exception) {
                _errorMessage.postValue("Something went wrong. Please try again later.")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun parseErrorMessage(statusCode: Int, errorBody: String?): String {
        return when (statusCode) {
            400 -> "Invalid request. Please check your input."
            401 -> "Unauthorized access. Please check your email and password."
            403 -> "Your account is restricted. Contact support for assistance."
            404 -> "Server not found. Please try again later."
            500 -> "Internal server error. Please try again later."
            else -> errorBody ?: "Unexpected error occurred. Please try again."
        }
    }

}
