package com.github.leowroth.photos_gallery.ui.photoslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.leowroth.photos_gallery.IoDispatcher
import com.github.leowroth.photos_gallery.MainDispatcher
import com.github.leowroth.photos_gallery.domain.model.Photo
import com.github.leowroth.photos_gallery.domain.usecase.GetPhotosUseCase
import com.github.leowroth.photos_gallery.domain.usecase.InsertAllPhotosUseCase
import com.github.leowroth.photos_gallery.domain.usecase.RefreshPhotosUseCase
import com.github.leowroth.photos_gallery.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okio.IOException

class PhotosListViewModel
@ViewModelInject constructor(
    private val getPhotosUseCaseImpl: GetPhotosUseCase,
    private val refreshPhotosUseCaseImpl: RefreshPhotosUseCase,
    private val insertAllPhotosUseCaseImpl: InsertAllPhotosUseCase,
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {
    val photosList = getPhotosUseCaseImpl.invoke()
    private var eventNetworkError = MutableLiveData(false)
    val eventNetworkErrorData: LiveData<Boolean> get() = eventNetworkError
    private var eventLoading = MutableLiveData(false)
    val eventLoadingData: LiveData<Boolean> get() = eventLoading

    fun initDataFromRepository() {
        if (photosList.value.isNullOrEmpty()) refreshData()
    }

    fun forceRefreshDataFromRepository() {
        refreshData()
    }

    private fun refreshData() {
        viewModelScope.launch(ioDispatcher) {
            eventLoading.value = true
            eventNetworkError.value = false

            try {
                refreshPhotosUseCaseImpl.invoke()
            } catch (networkError: IOException) {
                // This delay prevents the Snackbar from being instantly
                //  dismissed, when the internet is completely off
                delay(500L)
                eventNetworkError.value = true
            } finally {
                eventLoading.value = false
            }
        }
    }

    fun onPhotoClicked(position: Int) {
        viewModelScope.launch(ioDispatcher) {
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