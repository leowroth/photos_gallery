package com.github.leowroth.photos_gallery.ui.photoslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.leowroth.photos_gallery.domain.model.Photo
import com.github.leowroth.photos_gallery.domain.usecase.GetPhotosUseCase
import com.github.leowroth.photos_gallery.domain.usecase.InsertAllPhotosUseCase
import com.github.leowroth.photos_gallery.domain.usecase.PhotoFavedUseCase
import com.github.leowroth.photos_gallery.domain.usecase.RefreshPhotosUseCase
import com.github.leowroth.photos_gallery.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException

class PhotosListViewModel
@ViewModelInject constructor(
    getPhotosUseCaseImpl: GetPhotosUseCase,
    private val refreshPhotosUseCaseImpl: RefreshPhotosUseCase,
    private val photoFavedUseCaseImpl: PhotoFavedUseCase,
    private val insertAllPhotosUseCaseImpl: InsertAllPhotosUseCase
) : BaseViewModel() {
    val photosList = getPhotosUseCaseImpl.invoke()
    private var eventNetworkError = MutableLiveData(false)
    val eventNetworkErrorData: LiveData<Boolean> get() = eventNetworkError
    private var eventLoading = MutableLiveData(false)
    val eventLoadingData: LiveData<Boolean> get() = eventLoading

    fun refreshDataFromRepository() {
        eventLoading.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                refreshPhotosUseCaseImpl.invoke()
            } catch (networkError: IOException) {
                viewModelScope.launch {
                    eventNetworkError.value = true
                    photosList.value.isNullOrEmpty()
                }
            } finally {
                viewModelScope.launch {
                    eventLoading.value = false
                }
            }
        }
    }

    fun onFavedClicked(position: Int) {
        photosList.value?.get(position)?.let {
            viewModelScope.launch(Dispatchers.IO) {
                photoFavedUseCaseImpl.photoFaved(it)
            }
        }
    }

    fun onPhotoClicked(position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            photosList.value?.get(position)?.let {
                val currentPhotos = photosList.value
                currentPhotos?.let {
                    val clickedPhoto = it.removeAt(position)
                    it.add(0, clickedPhoto)

                    val resultPhotos = mutableListOf<Photo>()
                    currentPhotos.forEachIndexed { index, photo ->
                        resultPhotos.add(photo.copy(position = index))
                    }
                    insertAllPhotosUseCaseImpl.insertAll(resultPhotos)
                }
            }
        }
    }
}