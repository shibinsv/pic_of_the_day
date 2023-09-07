package com.calibraint.picoftheday.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.calibraint.picoftheday.network.ApiCall
import com.calibraint.picoftheday.network.PictureResponse
import com.calibraint.picoftheday.utils.Constants
import com.calibraint.picoftheday.utils.SharedPreferences

class MainViewModel : ViewModel() {

    val presentData = MutableLiveData<PictureResponse>()

    /** Get data from shared preferences **/
    fun getCachedData(context: Context) {
        val data = SharedPreferences.readData(context, Constants.photoData)
        presentData.value = data
    }

    /** Get data from the API*/
    fun getData(onCompletion: () -> Unit, onError: (String) -> Unit) {
        ApiCall.getData(
            {
                presentData.postValue(it)
                onCompletion()
            },
            { error ->
                Log.e("MainViewModel", "getData: $error")
                onError(error)
            }
        )
    }
}