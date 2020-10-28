package com.github.weg_li_android.ui.photoslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import com.github.weg_li_android.data.repository.Repository
import com.github.weg_li_android.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers

class PhotosListViewModel
@ViewModelInject constructor(repository: Repository) : BaseViewModel() {

    val photosList = liveData(Dispatchers.IO) {
        val retrievedPhotosList = repository.getPhotosList()
        emit(retrievedPhotosList)
    }
}