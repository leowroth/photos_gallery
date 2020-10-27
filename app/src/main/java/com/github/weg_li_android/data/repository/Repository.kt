package com.github.weg_li_android.data.repository

import com.github.weg_li_android.data.api.PhotosEndpoints
import com.github.weg_li_android.data.api.ServiceBuilder

class Repository() {
    var service = ServiceBuilder.buildService(PhotosEndpoints::class.java)

    suspend fun getPhotosList() = service.getPhotosList()
}