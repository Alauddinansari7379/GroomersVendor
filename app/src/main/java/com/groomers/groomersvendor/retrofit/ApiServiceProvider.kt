package com.groomers.groomersvendor.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceProvider {

    companion object {
        private const val BASE_URL = "https://groomers.co.in/public/api/"

        fun getApiService(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}
