package com.github.weg_li_android.ui.photoslist

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.github.weg_li_android.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosListActivity : BaseActivity() {
    private lateinit var viewModel: PhotosListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(PhotosListViewModel::class.java)

        viewModel.photosList.observe(this, { list ->
            list.count()
        })
    }
}