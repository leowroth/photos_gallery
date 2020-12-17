package com.github.leowroth.photos_gallery.domain.usecase

import com.github.leowroth.photos_gallery.data.repository.PhotosRepository
import com.github.leowroth.photos_gallery.domain.model.Photo
import javax.inject.Inject

interface PhotoFavedUseCase {
    suspend fun photoFaved(photo: Photo)
}

class PhotoFavedUseCaseImpl @Inject constructor(
    private val repository: PhotosRepository

) : PhotoFavedUseCase {
    override suspend fun photoFaved(photo: Photo) = repository.photoFaved(photo)
}