package com.groomers.groomersvendor.retrofit

import com.example.tecexactly.model.modellogin.ModelLogin
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Response<ModelLogin>
}