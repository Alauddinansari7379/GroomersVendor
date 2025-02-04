package com.groomers.groomersvendor.retrofit

import ModelLogin
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Response<ModelLogin>
//    @Multipart
//    @POST("register")
//    suspend fun registerUser(
//        @Part("name") name: RequestBody,
//        @Part("mobile") mobile: RequestBody,
//        @Part("email") email: RequestBody,
//        @Part("password") password: RequestBody,
//        @Part("password_confirmation") passwordConfirmation: RequestBody,
//        @Part("role") role: RequestBody,
//        @Part("businessName") businessName: RequestBody,
//        @Part("businessCategory") businessCategory: RequestBody,
//        @Part("aboutBusiness") aboutBusiness: RequestBody,
//        @Part("teamSize") teamSize: RequestBody,
//        @Part("address") address: RequestBody,
//        @Part("city") city: RequestBody,
//        @Part("zipcode") zipcode: RequestBody,
//        @Part("idproofType") idproofType: RequestBody,
//        @Part("services") services: RequestBody,
//        @Part("latitude") latitude: RequestBody,
//        @Part("longitude") longitude: RequestBody,
//        @Part shopAgreement: MultipartBody.Part
//    ): Response<Unit>
}