package com.github.weg_li_android.ui.photoslist

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.github.weg_li_android.GlideApp
import com.github.weg_li_android.R
import com.github.weg_li_android.domain.model.Photo
import com.github.weg_li_android.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.photos_list.*
import timber.log.Timber

@AndroidEntryPoint
class PhotosListActivity : BaseActivity() {
    private lateinit var viewModel: PhotosListViewModel
    private lateinit var adapter: PhotosListAdapter
    private lateinit var photoList: List<Photo>
    private val amountToPreload = 10


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photos_list)

        viewModel = ViewModelProvider(this).get(PhotosListViewModel::class.java)

        viewModel.photosList.observe(this, { list ->
            val sizeProvider = ViewPreloadSizeProvider<Photo>()

            val glideRequest = GlideApp.with(applicationContext)
            val fullRequest =
                glideRequest.asDrawable().centerCrop().placeholder(ColorDrawable(Color.GRAY))


            adapter = PhotosListAdapter(list, sizeProvider, fullRequest)
            adapter.setHasStableIds(true)
            adapter.onItemClick = { position ->
                //TODO show PhotoDetailFragment
                Timber.d(list[position].author)
            }
            photosRecyclerView.adapter = adapter

            val preLoader = RecyclerViewPreloader(
                glideRequest, adapter, sizeProvider, amountToPreload
            )
            photosRecyclerView.addOnScrollListener(preLoader)
            photosRecyclerView.setItemViewCacheSize(0)
            photosRecyclerView.setRecyclerListener { holder ->
                glideRequest.clear(holder.itemView)
            }

            photoList = list
        })
    }
}