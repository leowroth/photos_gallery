package com.github.leowroth.photos_gallery.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.github.leowroth.photos_gallery.data.api.PhotosEndpoints
import com.github.leowroth.photos_gallery.data.api.ServiceBuilder
import com.github.leowroth.photos_gallery.data.api.asDatabaseModel
import com.github.leowroth.photos_gallery.data.database.PhotosDatabase
import com.github.leowroth.photos_gallery.data.database.asDomainModel
import com.github.leowroth.photos_gallery.domain.model.Photo
import com.github.leowroth.photos_gallery.domain.model.asDatabasePhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class PhotosRepository @Inject constructor(private val database: PhotosDatabase) {
    var service = ServiceBuilder.buildService(PhotosEndpoints::class.java)

    suspend fun refreshPhotos() {
        withContext(Dispatchers.IO) {

            val remotePhotos = service.getPhotosList()
            Timber.d("Inserting: ${remotePhotos.size}")
            database.photoDao().insertAll(remotePhotos.asDatabaseModel())

            val oldPhotos = database.photoDao().getCurrentPhotos()
            Timber.d("Updating: ${oldPhotos.size}")
            oldPhotos.let { it ->
                it.forEach { oldPhoto -> database.photoDao().update(oldPhoto) }
            }
        }
    }

    suspend fun photoFaved(currentPhoto: Photo) {
        withContext(Dispatchers.IO) {
            val copyPhoto = currentPhoto.copy(faved = currentPhoto.faved.not())
            database.photoDao().update(copyPhoto.asDatabasePhoto())
        }
    }

    val photos: LiveData<List<Photo>> = Transformations.map(database.photoDao().getPhotos()) {
        it.asDomainModel()
    }
}