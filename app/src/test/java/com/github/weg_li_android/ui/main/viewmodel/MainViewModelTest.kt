package com.github.weg_li_android.ui.main.viewmodel

import com.github.weg_li_android.data.repository.Repository
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before

class MainViewModelTest {

    lateinit var mainViewModel: MainViewModel
    private val mockRepository: Repository = mock()

    @Before
    fun setup() {
        mainViewModel = MainViewModel(mockRepository)
    }

}