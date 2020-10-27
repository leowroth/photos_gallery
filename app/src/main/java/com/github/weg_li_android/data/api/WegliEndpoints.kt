package com.github.weg_li_android.data.api

import com.github.weg_li_android.domain.model.Photo
import retrofit2.http.GET

interface PhotosEndpoints {

    @GET("/v2/list")
    suspend fun getPhotosList(): List<Photo>
}