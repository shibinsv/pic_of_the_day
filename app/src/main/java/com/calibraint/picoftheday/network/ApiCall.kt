package com.calibraint.picoftheday.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiCall {

    companion object {

        private val apiService: ApiService =
            RetrofitClient.getClient(ApiConstants.baseUrl).create(ApiService::class.java)

        /** Get data from the API*/
        fun getData(
            onSuccess: (PictureResponse?) -> Unit,
            onError: (String) -> Unit,
        ) {
            apiService.getData(ApiConstants.apiKey).enqueue(
                object : Callback<PictureResponse> {
                    override fun onResponse(
                        call: Call<PictureResponse>,
                        response: Response<PictureResponse>,
                    ) {
                        if (response.isSuccessful) {
                            onSuccess(response.body())
                        } else {
                            onError(response.errorBody().toString())
                        }
                    }

                    override fun onFailure(call: Call<PictureResponse>, t: Throwable) {
                        onError(t.message.toString())
                    }
                }
            )
        }
    }
}