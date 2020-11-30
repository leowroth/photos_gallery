package com.github.leowroth.photos_gallery.ui.photoslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.leowroth.photos_gallery.data.repository.PhotosRepository
import com.github.leowroth.photos_gallery.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import okio.IOException

class PhotosListViewModel
@ViewModelInject constructor(private val photosRepository: PhotosRepository) : BaseViewModel() {

    init {
        refreshDataFromRepository()
    }

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                photosRepository.refreshPhotos()
                _eventNetworkError.value = false
            } catch (networkError: IOException) {
                if (photosList.value.isNullOrEmpty()) _eventNetworkError.value = true
            }
        }
    }

    fun onFavedClicked(position: Int) {
        //TODO Update faved for the Photo at the given position
    }

    val photosList = photosRepository.photos
}