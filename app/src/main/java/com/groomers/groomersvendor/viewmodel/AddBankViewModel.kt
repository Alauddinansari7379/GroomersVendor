package com.groomers.groomersvendor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.groomers.groomersvendor.model.ModelGetVendor.ModelGetVendor
import com.groomers.groomersvendor.model.modeladdbank.ModelAddBank
import com.groomers.groomersvendor.model.modeladdhelp.ModelAddHelp
import com.groomers.groomersvendor.model.modelhelplist.ModelHelpList
import com.groomers.groomersvendor.retrofit.ApiService
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
@HiltViewModel
class AddBankViewModel@Inject constructor(
    private val apiService: ApiService,
    application: Application,
    private val sessionManager: SessionManager
) : AndroidViewModel(application) {

    private val _modelAddBank = MutableLiveData<ModelAddBank?>()
    val modelAddBank: LiveData<ModelAddBank?> = _modelAddBank

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun addBank(accountName : String, accountNo : String,bankName : String,ifsc : String,branch : String) {

        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = apiService.addBank("Bearer ${sessionManager.accessToken}", accountName, accountNo, bankName,ifsc, branch)

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()
                    if (responseBody!!.status == 1) {
                        _modelAddBank.postValue(responseBody)
                        sessionManager.isBank ="1"
                    } else {
                        _errorMessage.postValue(responseBody.message ?: "Something went wrong. Please try again.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.postValue(parseErrorMessage(response.code(), errorBody))
                }
            } catch (e: IOException) {
                _errorMessage.postValue("No internet connection. Please check your network.")
            } catch (e: HttpException) {
                _errorMessage.postValue("Server error (${e.code()}): Please try again later.")
            } catch (e: Exception) {
                _errorMessage.postValue("Something went wrong. Please try again.")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }




    private fun parseErrorMessage(statusCode: Int, errorBody: String?): String {
        return when (statusCode) {
            400 -> "Invalid request. Please check your input."
            401 -> "Session expired. Please log in again."
            403 -> "Access denied. Contact support."
            404 -> "Server not found. Try again later."
            500 -> "Server error. Please try again later."
            else -> errorBody ?: "Unexpected error occurred. Please try again."
        }
    }
    fun clearRegisterData() {
        _modelAddBank.value = null
        _errorMessage.value =null
    }
}
