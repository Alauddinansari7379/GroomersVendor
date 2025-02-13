package com.groomers.groomersvendor.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.groomers.groomersvendor.model.modelregister.ModelRegister
import com.groomers.groomersvendor.retrofit.ApiService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    var name: String? =null
    var yourName: String? =null
   var mobile: String? =null
   var email: String? =null
   var password: String? =null
   var userId: String? =null
   var passwordConfirmation: String? =null
   var role: String? =null
   var businessName: String? =null
   var businessCategory: String? =null
   var aboutBusiness: String? =null
   var teamSize: String? =null
   var address1: String? =null
   var address2: String? =null
   var mapUrl: String? =null
   var city: String? =null
   var zipcode: String? =null
   var idProofImagePath: String? =null
   var idproofType: String? =null
   var services: String? =null
   var latitude: String? =null
   var longitude: String? =null
   var country: String? =null
   var state: String? =null
   var shopAgreement : String? =null

    private val _modelRegister = MutableLiveData<ModelRegister>()
    val modelRegister: LiveData<ModelRegister> = _modelRegister

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(
        apiService: ApiService,
        name: String, mobile: String, email: String, password: String,
        passwordConfirmation: String, role: String, businessName: String,
        businessCategory: String, aboutBusiness: String, teamSize: String,
        address: String, city: String, zipcode: String, idproofType: String,
        services: String, latitude: String, longitude: String, shopAgreement: MultipartBody.Part
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true) // Show loading state
            try {
                val response = apiService.registerUser(
                    name, mobile, email, password, passwordConfirmation, role, businessName,
                    "4", aboutBusiness, "7", address, city, zipcode, idproofType,
                    services, latitude, longitude,"23","23","45","username" ,shopAgreement
                )

                _isLoading.postValue(false) // Hide loading state

                if (response.isSuccessful) {
                    if (response.body()?.status ==1) {
                        _modelRegister.postValue(response.body())
                    }else{
                        _errorMessage.postValue("Error: ${response.message()}")
                        _isLoading.postValue(false)

                    }
                } else {
                    _errorMessage.postValue("Error: ${response.message()}")
                    _isLoading.postValue(false)
                }
            } catch (e: Exception) {
                _isLoading.postValue(false)
                _errorMessage.postValue("Exception: ${e.localizedMessage}")
                Log.e("RegisterViewModel", "Error: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
