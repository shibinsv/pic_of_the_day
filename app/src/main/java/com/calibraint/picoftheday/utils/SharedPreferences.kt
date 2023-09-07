package com.calibraint.picoftheday.utils

import android.content.Context
import com.calibraint.picoftheday.network.PictureResponse
import com.google.gson.Gson

class SharedPreferences {
    companion object {

        /** Update data to shared preferences*/
        fun updatePrefs(context: Context, key: String, data: String) {
            val sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE)
            val photoData = Gson().fromJson(data, PictureResponse::class.java)
            val editor = sharedPref.edit()
            photoData.apply {
                editor.putString(Constants.title, title)
                editor.putString(Constants.description, description)
                editor.putString(Constants.date, date)
                editor.putString(Constants.media, media)
                editor.putString(Constants.mediaType, mediaType)
            }
            editor.apply()
        }

        /** Read data to shared preferences*/
        fun readData(context: Context, key: String): PictureResponse {
            val sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE)
            val data = PictureResponse()
            data.apply {
                title = sharedPref.getString(Constants.title, null)
                date = sharedPref.getString(Constants.date, null)
                description = sharedPref.getString(Constants.description, null)
                media = sharedPref.getString(Constants.media, null)
                mediaType = sharedPref.getString(Constants.mediaType, null)
            }
            return data
        }
    }
}