package com.github.leowroth.photos_gallery.domain.usecase

import com.github.leowroth.photos_gallery.data.repository.PhotosRepository
import javax.inject.Inject

interface RefreshPhotosUseCase {
    suspend operator fun invoke()
}

class RefreshPhotosUseCaseImpl @Inject constructor(private val repository: PhotosRepository) :
    RefreshPhotosUseCase {
    override suspend fun invoke() = repository.refreshPhotos()
}