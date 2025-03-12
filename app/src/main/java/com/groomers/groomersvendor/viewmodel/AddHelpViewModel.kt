package com.groomers.groomersvendor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.groomers.groomersvendor.model.modeladdhelp.ModelAddHelp
import com.groomers.groomersvendor.model.modelhelplist.ModelHelpList
import com.groomers.groomersvendor.retrofit.ApiService
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
@HiltViewModel
class AddHelpViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
    private val sessionManager: SessionManager
) : AndroidViewModel(application) {

    private val _modelAddHelp = MutableLiveData<ModelAddHelp?>()
    val modelAddHelp: LiveData<ModelAddHelp?> = _modelAddHelp

    private val _modelHelpList = MutableLiveData<ModelHelpList?>()
    val modelHelpList: LiveData<ModelHelpList?> = _modelHelpList

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun addHelp(name: String, mobile: String, query: String,description : String, imageUrl: MultipartBody.Part) {

        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = apiService.addHelp("Bearer ${sessionManager.accessToken}", name, mobile, query,description, imageUrl)

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()
                    if (responseBody!!.status == 1) {
                        _modelAddHelp.postValue(responseBody)
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


    fun getHelpList() {

        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = apiService.getHelpList("Bearer ${sessionManager.accessToken}")

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()
                    if (responseBody!!.status == 1) {
                        _modelHelpList.postValue(responseBody)
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
        _modelAddHelp.value = null
        _modelHelpList.value = null
        _errorMessage.value =null
    }
}
