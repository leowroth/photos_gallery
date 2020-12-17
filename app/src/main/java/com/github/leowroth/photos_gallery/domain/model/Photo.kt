package com.github.leowroth.photos_gallery.domain.model

import com.github.leowroth.photos_gallery.data.database.DatabasePhoto

data class Photo(
    val id: String,
    val title: String,
    val faved: Boolean,
    val downloadUrl: String
)

fun Photo.asDatabasePhoto(): DatabasePhoto {
    return DatabasePhoto(
        id = id,
        title = title,
        faved = faved,
        downloadUrl = downloadUrl
    )
}