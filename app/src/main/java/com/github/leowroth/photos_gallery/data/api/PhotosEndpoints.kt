package com.github.leowroth.photos_gallery.data.api

import retrofit2.http.GET

interface PhotosEndpoints {

    @GET("/v2/list")
    suspend fun getPhotosList(): NetworkPhotoContainer
}