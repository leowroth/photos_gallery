package com.github.weg_li_android.ui.photoslist

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.github.weg_li_android.R
import com.github.weg_li_android.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.photos_list.*
import timber.log.Timber

@AndroidEntryPoint
class PhotosListActivity : BaseActivity() {
    private lateinit var viewModel: PhotosListViewModel
    private lateinit var adapter: PhotosListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photos_list)

        viewModel = ViewModelProvider(this).get(PhotosListViewModel::class.java)

        viewModel.photosList.observe(this, { list ->
            val photosRecyclerView = photosRecyclerView
            adapter = PhotosListAdapter(
                list.map { it -> it.downloadUrl }
            )
            adapter.onItemClick = { position ->
                //TODO show PhotoDetailFragment
                Timber.d(list[position].author)
            }
            photosRecyclerView.adapter = adapter
        })
    }
}