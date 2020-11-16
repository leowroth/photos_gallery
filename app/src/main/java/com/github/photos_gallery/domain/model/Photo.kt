package com.github.photos_gallery.domain.model

import com.google.gson.annotations.SerializedName

data class Photo(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    var faved: Boolean,
    @SerializedName("download_url") val downloadUrl: String
)
