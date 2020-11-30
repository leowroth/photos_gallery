package com.github.leowroth.photos_gallery.ui.photoslist

import androidx.hilt.lifecycle.ViewModelInject
import com.github.leowroth.photos_gallery.data.repository.PhotosRepository
import com.github.leowroth.photos_gallery.ui.base.BaseViewModel

class PhotosListViewModel
@ViewModelInject constructor(photosRepository: PhotosRepository) : BaseViewModel() {
    fun onFavedClicked(position: Int) {
        //TODO Update faved for the Photo at the given position
    }

    val photosList = photosRepository.photos
}