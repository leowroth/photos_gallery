package com.github.leowroth.photos_gallery.domain.usecase

import androidx.lifecycle.LiveData
import com.github.leowroth.photos_gallery.data.repository.PhotosRepository
import com.github.leowroth.photos_gallery.domain.model.Photo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

interface GetPhotosUseCase {
    operator fun invoke(): LiveData<List<Photo>>
}

class GetPhotosUseCaseImpl @Inject constructor(private val repository: PhotosRepository) :
    GetPhotosUseCase {
    override fun invoke(): LiveData<List<Photo>> = repository.photos
}

@Module
@InstallIn(ActivityComponent::class)
abstract class GetPhotosUseCaseModule {

    @Binds
    abstract fun bindGetPhotosUseCase(getPhotosUseCaseImpl: GetPhotosUseCaseImpl): GetPhotosUseCase
}