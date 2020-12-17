package com.github.leowroth.photos_gallery.ui.photoslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.leowroth.photos_gallery.domain.usecase.GetPhotosUseCase
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
    private val photoFavedUseCaseImpl: PhotoFavedUseCase
) : BaseViewModel() {
    val photosList = getPhotosUseCaseImpl.invoke()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            refreshPhotosUseCaseImpl.invoke()
        }
    }

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private fun refreshDataFromRepository() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                refreshPhotosUseCaseImpl.invoke()
                _eventNetworkError.value = false
            } catch (networkError: IOException) {
                if (photosList.value.isNullOrEmpty()) _eventNetworkError.value = true
            }
        }
    }

    fun onFavedClicked(position: Int) {
        val currentPhoto = photosList.value?.get(position)
        if (currentPhoto != null) {
            viewModelScope.launch(Dispatchers.IO) {
                photoFavedUseCaseImpl.photoFaved(currentPhoto)
            }
        }
    }
}