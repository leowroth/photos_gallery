package com.github.weg_li_android.ui.main.viewmodel

import com.github.weg_li_android.data.repository.Repository
import com.github.weg_li_android.ui.base.BaseViewModel
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