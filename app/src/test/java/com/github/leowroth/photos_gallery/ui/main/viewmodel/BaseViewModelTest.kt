package com.github.leowroth.photos_gallery.ui.main.viewmodel

import com.github.leowroth.photos_gallery.data.repository.PhotosRepository
import com.github.leowroth.photos_gallery.ui.base.BaseViewModel
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before

class BaseViewModelTest {

    lateinit var baseViewModel: BaseViewModel
    private val mockPhotosRepository: PhotosRepository = mock()

    @Before
    fun setup() {
//        baseViewModel = BaseViewModel(mockRepository)
    }

}