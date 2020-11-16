package com.github.photos_gallery.ui.main.viewmodel

import com.github.photos_gallery.data.repository.Repository
import com.github.photos_gallery.ui.base.BaseViewModel
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before

class BaseViewModelTest {

    lateinit var baseViewModel: BaseViewModel
    private val mockRepository: Repository = mock()

    @Before
    fun setup() {
//        baseViewModel = BaseViewModel(mockRepository)
    }

}