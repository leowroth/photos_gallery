package com.github.photos_gallery.data.api

import com.github.photos_gallery.domain.model.Photo
import retrofit2.http.GET

interface PhotosEndpoints {

    @GET("/v2/list")
    suspend fun getPhotosList(): List<Photo>
}