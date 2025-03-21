package com.groomers.groomersvendor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.groomers.groomersvendor.model.modelupdateprfphoto.ModelUpdateProfPhoto
import com.groomers.groomersvendor.retrofit.ApiService
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uploadResult = MutableLiveData<String?>()
    val uploadResult: LiveData<String?> get() = _uploadResult

    fun uploadProfilePicture(file: File) {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        viewModelScope.launch {
            _uploadResult.postValue("Uploading...")
            try {
                val response = apiService.uploadProfilePicture("Bearer ${sessionManager.accessToken}",body)

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!
                    if (responseBody.status == 1) {
                        val newProfileUrl = responseBody.result.profile_picture // Assuming API returns the updated URL

                        // Store new profile picture URL in session
                        sessionManager.profilePictureUrl = newProfileUrl
                        _uploadResult.postValue(responseBody.message)
                    } else {
                        _uploadResult.postValue("Error: ${responseBody.message}")
                    }
                } else {
                    _uploadResult.postValue(parseErrorMessage(response))
                }
            } catch (e: IOException) {
                _uploadResult.postValue("No internet connection. Please try again.")
            } catch (e: HttpException) {
                _uploadResult.postValue("Server error (${e.code()}): ${e.message()}")
            } catch (e: Exception) {
                _uploadResult.postValue(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    private fun parseErrorMessage(response: Response<ModelUpdateProfPhoto>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            when (response.code()) {
                400 -> "Invalid request. Please check the image format."
                401 -> "Unauthorized access. Please log in again."
                403 -> "Access denied. You do not have permission."
                404 -> "Server not found. Try again later."
                500 -> "Internal server error. Please try again later."
                else -> errorBody ?: "Unexpected error occurred. Please try again."
            }
        } catch (e: Exception) {
            "Error processing response"
        }
    }

    fun uploadCoverPicture(file: File) {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        viewModelScope.launch {
            _uploadResult.postValue("Uploading...")
            try {
                val response = apiService.uploadCoverPicture("Bearer ${sessionManager.accessToken}",body)

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!
                    if (responseBody.status == 1) {
                        val newProfileUrl = responseBody.result.vendorCoverImage // Assuming API returns the updated URL

                        // Store new profile picture URL in session
                        sessionManager.coverPictureUrl = newProfileUrl
                        _uploadResult.postValue(responseBody.message)
                    } else {
                        _uploadResult.postValue("Error: ${responseBody.message}")
                    }
                } else {
                    _uploadResult.postValue(parseErrorMessage(response))
                }
            } catch (e: IOException) {
                _uploadResult.postValue("No internet connection. Please try again.")
            } catch (e: HttpException) {
                _uploadResult.postValue("Server error (${e.code()}): ${e.message()}")
            } catch (e: Exception) {
                _uploadResult.postValue(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    fun clearRegisterData() {
        _uploadResult.value = null
    }
}
