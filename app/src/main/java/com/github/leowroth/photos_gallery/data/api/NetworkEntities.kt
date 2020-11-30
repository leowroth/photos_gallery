package com.github.leowroth.photos_gallery.data.api

import com.github.leowroth.photos_gallery.data.database.DatabasePhoto

data class NetworkPhotoContainer(val photos: List<NetworkPhoto>)

data class NetworkPhoto(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val download_url: String
)

fun NetworkPhotoContainer.asDatabaseModel(): List<DatabasePhoto> {
    return photos.map {
        DatabasePhoto(
            id = it.id,
            author = it.author,
            width = it.width,
            height = it.height,
            url = it.url,
            faved = false,
            downloadUrl = it.download_url
        )
    }
}