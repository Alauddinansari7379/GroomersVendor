package com.groomers.groomersvendor.retrofit

import ModelLogin
import com.groomers.groomersvendor.model.modelregister.ModelRegister
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Response<ModelLogin>
    @Multipart
    @POST("register")
    suspend fun registerUser(
        @Query("name") name: String,
        @Query("mobile") mobile: String,
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("password_confirmation") passwordConfirmation: String,
        @Query("role") role: String,
        @Query("businessName") businessName: String,
        @Query("businessCategory") businessCategory: String,
        @Query("aboutBusiness") aboutBusiness: String,
        @Query("teamSize") teamSize: String,
        @Query("address") address: String,
        @Query("city") city: String,
        @Query("zipcode") zipcode: String,
        @Query("idproofType") idproofType: String,
        @Query("services") services: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("country") country: String,  // New field added
        @Query("state") state: String,      // New field added
        @Part shopAgreement: MultipartBody.Part  // For file upload
    ): Response<ModelRegister>

}