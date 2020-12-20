package com.github.leowroth.photos_gallery.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.github.leowroth.photos_gallery.data.api.PhotosEndpoints
import com.github.leowroth.photos_gallery.data.api.ServiceBuilder
import com.github.leowroth.photos_gallery.data.api.asDatabaseModel
import com.github.leowroth.photos_gallery.data.database.DatabasePhoto
import com.github.leowroth.photos_gallery.data.database.PhotosDatabase
import com.github.leowroth.photos_gallery.data.database.asDomainModel
import com.github.leowroth.photos_gallery.domain.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotosRepository @Inject constructor(private val database: PhotosDatabase) {


    suspend fun refreshPhotos() {
        withContext(Dispatchers.IO) {
            val service =
                ServiceBuilder.buildService(PhotosEndpoints::class.java)
            val remotePhotos = service.getPhotosList()
            database.photoDao().insertAllIgnore(remotePhotos.asDatabaseModel())
        }
    }

    suspend fun insertAll(photos: List<DatabasePhoto>) {
        withContext(Dispatchers.IO) {
            database.photoDao().insertAllReplace(photos)
        }
    }

    val photos: LiveData<MutableList<Photo>> =
        Transformations.map(database.photoDao().getPhotos()) { databasePhotos ->
            databasePhotos.asDomainModel().sortedBy { it.position }
                .toMutableList()
        }
}