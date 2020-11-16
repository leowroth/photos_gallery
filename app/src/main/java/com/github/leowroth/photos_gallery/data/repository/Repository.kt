package com.github.leowroth.photos_gallery.data.repository

import com.github.leowroth.photos_gallery.data.api.PhotosEndpoints
import com.github.leowroth.photos_gallery.data.api.ServiceBuilder
import javax.inject.Inject

class Repository @Inject constructor() {
    var service = ServiceBuilder.buildService(PhotosEndpoints::class.java)

    suspend fun getPhotosList() = service.getPhotosList()
}