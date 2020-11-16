package com.github.leowroth.photos_gallery.ui.photoslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import com.github.leowroth.photos_gallery.data.repository.Repository
import com.github.leowroth.photos_gallery.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers

class PhotosListViewModel
@ViewModelInject constructor(repository: Repository) : BaseViewModel() {
    fun onFavedClicked(position: Int) {
        //TODO Update faved for the Photo at the given position
    }

    val photosList = liveData(Dispatchers.IO) {
        val retrievedPhotosList = repository.getPhotosList()
        emit(retrievedPhotosList)
    }
}