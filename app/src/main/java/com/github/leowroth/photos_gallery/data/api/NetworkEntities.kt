package com.github.leowroth.photos_gallery.data.api

import com.github.leowroth.photos_gallery.data.database.DatabasePhoto


data class NetworkPhoto(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val download_url: String
)

fun List<NetworkPhoto>.asDatabaseModel(): List<DatabasePhoto> {
    return map {
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