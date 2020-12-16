package com.github.leowroth.photos_gallery.data.api

import retrofit2.http.GET

interface PhotosEndpoints {

    @GET("/cats")
    suspend fun getPhotosList(): List<NetworkPhoto>
}