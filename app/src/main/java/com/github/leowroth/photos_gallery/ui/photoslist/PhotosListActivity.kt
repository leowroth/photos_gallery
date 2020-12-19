package com.github.leowroth.photos_gallery.ui.photoslist

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
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

        viewModel.eventLoadingData.observe(this, {
            if (it) progressCircular.visibility = View.VISIBLE
            else progressCircular.visibility = View.INVISIBLE
        })

        viewModel.eventNetworkErrorData.observe(this, {

            //TODO show error
            if (it) Timber.e("INTERNET DOWN INTERNET DOWN!")
        })

        viewModel.refreshDataFromRepository()

        viewModel.photosList.observe(this, { photosList ->
            val glideRequest = GlideApp.with(applicationContext)
            val fullRequest =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    glideRequest.asDrawable().centerCrop()
                        .placeholder(ColorDrawable(getColor(R.color.primaryColor)))
                } else {
                    glideRequest.asDrawable().centerCrop()
                        .placeholder(ColorDrawable(Color.MAGENTA))
                }

            val sizeProvider = ViewPreloadSizeProvider<Photo>()

            val adapter = setupAdapter(photosList, sizeProvider, fullRequest)
            setupRecyclerView(adapter, glideRequest, sizeProvider)
        })

    }

    private fun setupAdapter(
        photosList: List<Photo>,
        sizeProvider: ViewPreloadSizeProvider<Photo>,
        fullRequest: GlideRequest<Drawable>
    ): PhotosListAdapter {
        val adapter = PhotosListAdapter(photosList, sizeProvider, fullRequest)
        adapter.setHasStableIds(true)
        adapter.onItemClick = { position ->
            //TODO show PhotoDetailFragment
            Timber.d(photosList[position].title)
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
        photosRecyclerView.swapAdapter(adapter, false)

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