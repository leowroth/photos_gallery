package com.github.weg_li_android.ui.photoslist

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.github.weg_li_android.GlideRequest
import com.github.weg_li_android.databinding.PhotoCellBinding
import com.github.weg_li_android.domain.model.Photo

class PhotosListAdapter(
    private val photos: List<Photo>,
    private val preloadSizeProvider: ViewPreloadSizeProvider<Photo>,
    private val fullRequest: GlideRequest<Drawable>,
    var onItemClick: ((Int) -> Unit)? = null,
) : RecyclerView.Adapter<PhotosListAdapter.MyViewHolder>(),
    ListPreloader.PreloadSizeProvider<Photo>, ListPreloader.PreloadModelProvider<Photo> {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myViewHolder = MyViewHolder(
            PhotoCellBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        preloadSizeProvider.setView(myViewHolder.itemView)
        return myViewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentUrl = photos[position].downloadUrl
        val photosListImageView = holder.binding.photosListImageView

        fullRequest.load(currentUrl).into(photosListImageView)
    }

    override fun getItemId(position: Int): Long {
        return photos[position].id.toLong()
    }

    override fun getItemCount() = photos.size

    inner class MyViewHolder(val binding: PhotoCellBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }
    }

    override fun getPreloadSize(
        item: Photo,
        adapterPosition: Int,
        perItemPosition: Int
    ): IntArray? {
        return intArrayOf(item.width, item.height)
    }

    override fun getPreloadItems(position: Int): MutableList<Photo> {
        return photos.subList(position, position + 1).toMutableList()
    }

    override fun getPreloadRequestBuilder(item: Photo): RequestBuilder<*>? {
        return fullRequest.load(item.downloadUrl)
    }
}