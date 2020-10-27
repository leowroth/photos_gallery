package com.github.weg_li_android.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.github.weg_li_android.data.repository.Repository
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val repository: Repository) : ViewModel() {

    val photos = liveData(Dispatchers.IO) {
        val retrievedPhotos = repository.getPhotosList()

        emit(retrievedPhotos)
    }
}