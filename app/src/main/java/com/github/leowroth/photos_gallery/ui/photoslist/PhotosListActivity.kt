package com.github.leowroth.photos_gallery.ui.photoslist

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.github.leowroth.photos_gallery.GlideApp
import com.github.leowroth.photos_gallery.GlideRequest
import com.github.leowroth.photos_gallery.GlideRequests
import com.github.leowroth.photos_gallery.R
import com.github.leowroth.photos_gallery.domain.model.Photo
import com.github.leowroth.photos_gallery.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.photos_list.*
import timber.log.Timber

@AndroidEntryPoint
class PhotosListActivity : BaseActivity() {
    private val amountToPreload = 10
    private lateinit var viewModel: PhotosListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photos_list)

        viewModel = ViewModelProvider(this).get(PhotosListViewModel::class.java)

        viewModel.photosList.observe(this, { photosList ->
            val glideRequest = GlideApp.with(applicationContext)
            val fullRequest =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    glideRequest.asDrawable().centerCrop()
                        .placeholder(ColorDrawable(getColor(R.color.primaryColor)))
                } else {
                    glideRequest.asDrawable().centerCrop().placeholder(ColorDrawable(Color.MAGENTA))
                }

            val sizeProvider = ViewPreloadSizeProvider<Photo>()

            setupRecyclerView(
                setupAdapter(photosList, sizeProvider, fullRequest),
                glideRequest, sizeProvider
            )

        })
    }

    private fun setupAdapter(
        list: List<Photo>,
        sizeProvider: ViewPreloadSizeProvider<Photo>,
        fullRequest: GlideRequest<Drawable>
    ): PhotosListAdapter {
        val adapter = PhotosListAdapter(list, sizeProvider, fullRequest)
        adapter.setHasStableIds(true)
        adapter.onItemClick = { position ->
            //TODO show PhotoDetailFragment
            Timber.d(list[position].author)
        }
        adapter.onFaved = { position ->
            viewModel.onFavedClicked(position)
        }
        return adapter
    }

    private fun setupRecyclerView(
        adapter: PhotosListAdapter,
        glideRequest: GlideRequests,
        sizeProvider: ViewPreloadSizeProvider<Photo>
    ) {
        photosRecyclerView.adapter = adapter

        val preLoader = RecyclerViewPreloader(
            glideRequest, adapter, sizeProvider, amountToPreload
        )
        photosRecyclerView.addOnScrollListener(preLoader)
        photosRecyclerView.setItemViewCacheSize(0)
        photosRecyclerView.setRecyclerListener { holder ->
            glideRequest.clear(holder.itemView)
        }
    }
}