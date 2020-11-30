package com.github.leowroth.photos_gallery.domain.model

import com.github.leowroth.photos_gallery.data.database.DatabasePhoto

data class Photo(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val faved: Boolean,
    val downloadUrl: String
)

fun Photo.asDatabasePhoto(): DatabasePhoto {
    return DatabasePhoto(
        id = id,
        author = author,
        width = width,
        height = height,
        url = url,
        faved = faved,
        downloadUrl = downloadUrl
    )
}