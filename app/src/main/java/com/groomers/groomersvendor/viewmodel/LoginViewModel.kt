package com.groomers.groomersvendor.viewmodel

import ModelLogin
import android.app.Application
import androidx.lifecycle.*

import com.groomers.groomersvendor.retrofit.ApiService
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
    private val sessionManager: SessionManager
) : AndroidViewModel(application)  {

    private lateinit var response: Response<ModelLogin>
    private val _modelLogin = MutableLiveData<ModelLogin?>()
    val modelLogin: LiveData<ModelLogin?> = _modelLogin

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                if (email.contains("@gmail.com")) {
                     response = apiService.login(email, password,"vendor")
                }else{
                    response = apiService.loginWithUsername(email,password,"vendor")
                }

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()
                    if (!responseBody?.access_token.isNullOrEmpty()) {
                        _modelLogin.postValue(responseBody)
                        sessionManager.accessToken = responseBody!!.access_token
                        sessionManager.isLogin = true
                    } else {
                        _errorMessage.postValue("Incorrect email or password. Please try again.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = parseErrorMessage(response.code(), errorBody)
                    _errorMessage.postValue(errorMessage)
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

    fun getStoredToken(): String? {
        return sessionManager.accessToken
    }

    fun checkLoginStatus(): Boolean {
        return sessionManager.isLogin
    }

    fun logout() {
        sessionManager.clearSession()
    }
}
