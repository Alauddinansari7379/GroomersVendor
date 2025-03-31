package com.groomers.groomersvendor.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceProvider {

    companion object {
        private const val BASE_URL = "https://groomers.co.in/api/"
        const val IMAGE_URL = "https://groomers.co.in/public/uploads/"
        private val client: Retrofit? = null


        fun getApiService(): ApiService {
            // Create a logging interceptor
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            // Create an OkHttpClient and add the interceptor
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            // Create Retrofit instance with OkHttpClient
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient) // Add the OkHttpClient to Retrofit
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}
