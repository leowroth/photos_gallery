package com.github.leowroth.photos_gallery.domain.usecase

import com.github.leowroth.photos_gallery.data.repository.PhotosRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

interface RefreshPhotosUseCase {
    suspend operator fun invoke()
}

class RefreshPhotosUseCaseImpl @Inject constructor(private val repository: PhotosRepository) :
    RefreshPhotosUseCase {
    override suspend fun invoke() = repository.refreshPhotos()
}

@Module
@InstallIn(ActivityComponent::class)
abstract class RefreshPhotoUseCaseModule {

    @Binds
    abstract fun bindRefreshPhotoUseCase(refreshPhotosUseCaseImpl: RefreshPhotosUseCaseImpl): RefreshPhotosUseCase
}