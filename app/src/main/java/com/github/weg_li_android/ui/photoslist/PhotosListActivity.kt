package com.github.weg_li_android.ui.photoslist

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.github.weg_li_android.R
import com.github.weg_li_android.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.photos_list.*

@AndroidEntryPoint
class PhotosListActivity : BaseActivity(), PhotosListAdapter.PhotosClickListener {
    private lateinit var viewModel: PhotosListViewModel
    private lateinit var adapter: PhotosListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photos_list)

        viewModel = ViewModelProvider(this).get(PhotosListViewModel::class.java)

        viewModel.photosList.observe(this, { list ->
            val photosRecyclerView = photosRecyclerView
            photosRecyclerView.layoutManager = GridLayoutManager(this, 3)
            adapter = PhotosListAdapter(
                list.map { it -> it.downloadUrl },
                LayoutInflater.from(this),
                this
            )
            photosRecyclerView.adapter = adapter
        })
    }

    override fun onPhotoClicked(imageView: ImageView, position: Int) {
        TODO("Not yet implemented")
    }
}