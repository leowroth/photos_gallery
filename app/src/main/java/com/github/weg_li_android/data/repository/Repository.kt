package com.github.weg_li_android.data.repository

import com.github.weg_li_android.data.api.PhotosEndpoints
import com.github.weg_li_android.data.api.ServiceBuilder
import javax.inject.Inject

class Repository @Inject constructor() {
    var service = ServiceBuilder.buildService(PhotosEndpoints::class.java)

    suspend fun getPhotosList() = service.getPhotosList()
}