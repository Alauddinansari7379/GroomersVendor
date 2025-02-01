package com.groomers.groomersvendor.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.tecexactly.model.modellogin.ModelLogin
import com.groomers.groomersvendor.retrofit.ApiService
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionManager = SessionManager(application)

    private val _modelLogin = MutableLiveData<ModelLogin?>()
    val modelLogin: LiveData<ModelLogin?> = _modelLogin

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(apiService: ApiService, email: String, password: String) {
        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = apiService.login(email, password)

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()
                    if (!responseBody?.access_token.isNullOrEmpty()) {
                        _modelLogin.postValue(responseBody)
                        sessionManager.accessToken = responseBody!!.access_token
                        sessionManager.isLogin = true
                    } else {
                        _errorMessage.postValue("Invalid login credentials")
                    }
                } else {
                    _errorMessage.postValue("Failed to login: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error: ${e.message}")
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
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
