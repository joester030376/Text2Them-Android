package com.smartparking.app.rest

import android.util.Log
import com.bmd.mybmd.api.ApiService
import com.bmd.mybmd.api.UrlConstant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitRestClient {
    private const val TIME = 50
    private var baseApiService: ApiService? = null
    private var httpClient: OkHttpClient? = null


    private fun getHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient = OkHttpClient().newBuilder()
            .connectTimeout(TIME.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIME.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIME.toLong(), TimeUnit.SECONDS)
            .addNetworkInterceptor(Interceptor { chain ->
                val response = chain.proceed(chain.request())
                val body = response.body
                val bodyString = body!!.string()
                val contentType = body.contentType()
                Log.e("---->APIResponse", bodyString)
                response.newBuilder().body(bodyString.toResponseBody(contentType)).build()
            })
            .build()
        return httpClient as OkHttpClient
    }

    fun getInstance(): ApiService? {
        if (baseApiService == null) {
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(UrlConstant.BASE_URL)
                .addConverterFactory(ToStringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHttpClient())
                .build()
            baseApiService = retrofit.create(ApiService::class.java)
        }
        return baseApiService
    }
}