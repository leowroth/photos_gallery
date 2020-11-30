package com.github.leowroth.photos_gallery.domain.model

data class Photo(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val faved: Boolean,
    val downloadUrl: String
)