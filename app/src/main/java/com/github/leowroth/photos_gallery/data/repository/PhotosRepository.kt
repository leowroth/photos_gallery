package com.github.leowroth.photos_gallery.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.github.leowroth.photos_gallery.data.api.PhotosEndpoints
import com.github.leowroth.photos_gallery.data.api.ServiceBuilder
import com.github.leowroth.photos_gallery.data.api.asDatabaseModel
import com.github.leowroth.photos_gallery.data.database.PhotosDatabase
import com.github.leowroth.photos_gallery.data.database.asDomainModel
import com.github.leowroth.photos_gallery.domain.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotosRepository @Inject constructor(private val database: PhotosDatabase) {
    var service = ServiceBuilder.buildService(PhotosEndpoints::class.java)

    suspend fun refreshPhotos() {
        withContext(Dispatchers.IO) {
            val photos = service.getPhotosList()
            database.photoDao().insertAll(photos.asDatabaseModel())
        }
    }

    val photos: LiveData<List<Photo>> = Transformations.map(database.photoDao().getPhotos()) {
        it.asDomainModel()
    }
}