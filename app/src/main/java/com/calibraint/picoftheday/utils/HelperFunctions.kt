package com.calibraint.picoftheday.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

object HelperFunctions {

    /** Display a long toast with message provided */
    fun showToast(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    /** Capitalize the first letter of the String */
    fun capitalize(input: String): String {
        val capitalized = input.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        return capitalized
    }

    /** Extract video */
    fun extractVideoId(embedUrl: String): String {
        return try {
            val regex = Regex("""/embed/([A-Za-z0-9_-]+)\?""")
            val matchResult = regex.find(embedUrl)
            matchResult?.groupValues?.get(1) ?: embedUrl
        } catch (e: Exception) {
            embedUrl
        }
    }
}