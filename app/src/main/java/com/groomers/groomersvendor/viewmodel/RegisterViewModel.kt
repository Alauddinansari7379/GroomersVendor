package com.groomers.groomersvendor.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.groomers.groomersvendor.adapter.CategoryAdapter.Companion.categoryId
import com.groomers.groomersvendor.model.modelregister.ModelRegister
import com.groomers.groomersvendor.model.modeluserexist.ModelUserExist
import com.groomers.groomersvendor.retrofit.ApiClient.apiService
import com.groomers.groomersvendor.retrofit.ApiService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okio.IOException
import retrofit2.HttpException

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    var name: String? = null
    var yourName: String? = null
    var mobile: String? = null
    var email: String? = null
    var password: String? = null
    var userId: String? = null
    var passwordConfirmation: String? = null
    var role: String? = null
    var businessName: String? = null
    var businessCategory: String? = null
    var aboutBusiness: String? = null
    var teamSize: String? = null
    var address1: String? = null
    var address2: String? = null
    var mapUrl: String? = null
    var city: String? = null
    var zipcode: String? = null
    var idProofImagePath: String? = null
    var idproofType: String? = null
    var services: String? = null
    var latitude: String? = null
    var longitude: String? = null
    var country: String? = null
    var state: String? = null
    var shopAgreement: String? = null
    var accountName: String? = null
    var accountNo: String? = null
    var bankName: String? = null
    var ifsc: String? = null
    var branchName: String? = null

    private val _modelRegister = MutableLiveData<ModelRegister?>()
    val modelRegister: MutableLiveData<ModelRegister?> = _modelRegister

    private val _modelUserExist = MutableLiveData<ModelUserExist?>()
    val modelUserExist: MutableLiveData<ModelUserExist?> = _modelUserExist

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(
        apiService: ApiService,
        name: String,
        mobile: String,
        email: String,
        password: String,
        passwordConfirmation: String,
        role: String,
        businessName: String,
        businessCategory: String,
        aboutBusiness: String,
        teamSize: String,
        address: String,
        city: String,
        zipcode: String,
        idproofType: String,
        services: String,
        latitude: String,
        longitude: String,
        shopAgreement: MultipartBody.Part,
        language: String,
        userName: String,
        accountName: String,
        accountNumber: String,
        bankName: String,
        ifsc: String,
        branch: String,
        mapUrl: String,
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true) // Show loading state
            try {
                val response = apiService.registerUser(
                    name,
                    mobile,
                    email,
                    password,
                    passwordConfirmation,
                    role,
                    businessName,
                    businessCategory,
                    aboutBusiness,
                    teamSize,
                    address,
                    city,
                    zipcode,
                    idproofType,
                    services,
                    latitude,
                    longitude,
                    "",
                    "",
                    userName,
                    shopAgreement,
                    shopAgreement,
                    language,
                    accountName,
                    accountNumber,
                    bankName,
                    ifsc,
                    branch,
                    mapUrl
                )

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        if (body.status == 1) {
                            _modelRegister.postValue(body)
                        } else {
                            _errorMessage.postValue(
                                body.message ?: "Registration unsuccessful. Please try again."
                            )
                        }
                    } ?: run {
                        _errorMessage.postValue("Oops! Something went wrong. Please try again.")
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: response.message()
                    _errorMessage.postValue("Couldn't complete registration. Please check your details and try again.")
                }
            } catch (e: IOException) { // Handle network errors
                _errorMessage.postValue("No internet connection. Please check your network and try again.")
                Log.e("RegisterViewModel", "Network error: ${e.message}", e)
            } catch (e: HttpException) { // Handle HTTP errors
                _errorMessage.postValue("We're facing some issues on our end. Please try again later.")
                Log.e("RegisterViewModel", "HTTP error: ${e.message}", e)
            } catch (e: Exception) { // Handle unexpected errors
                _errorMessage.postValue("Something went wrong. Please try again.")
                Log.e("RegisterViewModel", "Unexpected error: ${e.message}", e)
            } finally {
                _isLoading.postValue(false) // Hide loading state
            }

        }

    }

    fun checkUserExist(email: String, userName: String) {
        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = apiService.checkUserExist(email, userName)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.status == 1) {
                        // User exists
                        _modelUserExist.postValue(responseBody)
                    } else {
                        // User doesn't exist or some business logic error
                        _errorMessage.postValue(responseBody?.message ?: "Unable to verify user. Please try again.")
                    }
                } else {
                    // Server returned an HTTP error
                    _errorMessage.postValue("Oops! Something went wrong: ${response.code()} ${response.message()}")
                }

            } catch (e: IOException) {
                // Network error like no internet
                _errorMessage.postValue("You're offline. Please check your internet connection.")
            } catch (e: HttpException) {
                // Unexpected HTTP protocol exception
                _errorMessage.postValue("A server error occurred. Please try again later.")
            } catch (e: Exception) {
                // Catch-all for other issues
                _errorMessage.postValue("Unexpected error occurred. Please try again.")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }


    fun clearRegisterData() {
        _modelRegister.value = null
        _errorMessage.value = null
        _modelUserExist.value = null
    }
}
