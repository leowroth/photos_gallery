package com.github.weg_li_android.ui.photoslist

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.github.weg_li_android.GlideRequest
import com.github.weg_li_android.databinding.PhotosItemBinding
import com.github.weg_li_android.domain.model.Photo

class PhotosListAdapter(
    private val photosList: List<Photo>,
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
            PhotosItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        preloadSizeProvider.setView(myViewHolder.itemView)
        return myViewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentUrl = photosList[position].downloadUrl
        val photosListImageView = holder.binding.photosListImageView

        fullRequest.load(currentUrl).into(photosListImageView)
    }

    override fun getItemId(position: Int): Long {
        return photosList[position].id.toLong()
    }

    override fun getItemCount() = photosList.size

    inner class MyViewHolder(val binding: PhotosItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }
    }

    override fun getPreloadSize(
        photo: Photo,
        adapterPosition: Int,
        perItemPosition: Int
    ): IntArray? {
        return intArrayOf(photo.width, photo.height)
    }

    override fun getPreloadItems(position: Int): MutableList<Photo> {
        return photosList.subList(position, position + 1).toMutableList()
    }

    override fun getPreloadRequestBuilder(item: Photo): RequestBuilder<*>? {
        return fullRequest.load(item.downloadUrl)
    }
}