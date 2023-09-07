package com.calibraint.picoftheday.network

import com.google.gson.annotations.SerializedName

data class PictureResponse(
    var title: String? = null,
    var date: String? = null,
    @SerializedName("explanation") var description: String? = null,
    var copyright: String? = null,
    @SerializedName("media_type") var mediaType: String? = null,
    @SerializedName("url") var media: String? = null,
    @SerializedName("hdurl") var imageHd: String? = null,
    @SerializedName("service_version") var serviceVersion: String? = null,
)