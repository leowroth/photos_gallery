package com.github.leowroth.photos_gallery.data.api

import com.github.leowroth.photos_gallery.data.database.DatabasePhoto


data class NetworkPhoto(
    val id: String,
    val title: String,
    val imageUrl: String
)

fun List<NetworkPhoto>.asDatabaseModel(): List<DatabasePhoto> {
    return map {
        DatabasePhoto(
            id = it.id,
            title = it.title,
            faved = false,
            downloadUrl = it.imageUrl.replace("http", "https")
        )
    }
}