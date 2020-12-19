package com.github.leowroth.photos_gallery.domain.usecase

import com.github.leowroth.photos_gallery.data.repository.PhotosRepository
import com.github.leowroth.photos_gallery.domain.model.Photo
import com.github.leowroth.photos_gallery.domain.model.asDatabasePhoto
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

interface InsertAllPhotosUseCase {
    suspend fun insertAll(photos: MutableList<Photo>)
}

class InsertAllPhotosUseCaseImpl @Inject constructor(
    private val repository: PhotosRepository
) : InsertAllPhotosUseCase {
    override suspend fun insertAll(photos: MutableList<Photo>) {
        repository.insertAll(photos.map { it.asDatabasePhoto() })
    }
}

@Module
@InstallIn(ActivityComponent::class)
abstract class InsertAllPhotosUseCaseModule {

    @Binds
    abstract fun bindInsertAllPhotosUseCase(InsertAllPhotosUseCaseImpl: InsertAllPhotosUseCaseImpl): InsertAllPhotosUseCase
}