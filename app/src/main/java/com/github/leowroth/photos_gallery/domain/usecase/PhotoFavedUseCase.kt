package com.github.leowroth.photos_gallery.domain.usecase

import com.github.leowroth.photos_gallery.data.repository.PhotosRepository
import com.github.leowroth.photos_gallery.domain.model.Photo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

interface PhotoFavedUseCase {
    suspend fun photoFaved(photo: Photo)
}

class PhotoFavedUseCaseImpl @Inject constructor(
    private val repository: PhotosRepository

) : PhotoFavedUseCase {
    override suspend fun photoFaved(photo: Photo) = repository.photoFaved(photo)
}

@Module
@InstallIn(ActivityComponent::class)
abstract class PhotoFavedUseCaseModule {

    @Binds
    abstract fun bindPhotoFavedUseCase(photoFavedUseCaseImpl: PhotoFavedUseCaseImpl): PhotoFavedUseCase
}