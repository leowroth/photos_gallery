package com.github.leowroth.photos_gallery.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.leowroth.photos_gallery.domain.model.Photo

@Entity
data class DatabasePhoto constructor(
    @PrimaryKey
    val id: String,
    val title: String,
    var faved: Boolean,
    val downloadUrl: String
)

fun List<DatabasePhoto>.asDomainModel(): List<Photo> {
    return map {
        Photo(
            id = it.id,
            title = it.title,
            faved = it.faved,
            downloadUrl = it.downloadUrl
        )
    }
}