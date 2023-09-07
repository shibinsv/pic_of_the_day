package com.calibraint.picoftheday.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}

interface ApiService {
    /** GET call to get the Media Data*/
    @GET(ApiConstants.end_point)
    fun getData(@Query(ApiConstants.api_key) apiKey: String): Call<PictureResponse>
}

