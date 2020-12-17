package com.github.leowroth.photos_gallery.ui.photoslist

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.github.leowroth.photos_gallery.GlideRequest
import com.github.leowroth.photos_gallery.R
import com.github.leowroth.photos_gallery.databinding.PhotosItemBinding
import com.github.leowroth.photos_gallery.domain.model.Photo

class PhotosListAdapter(
    private val photosList: List<Photo>,
    private val preloadSizeProvider: ViewPreloadSizeProvider<Photo>,
    private val fullRequest: GlideRequest<Drawable>,
    var onItemClick: ((Int) -> Unit)? = null,
    var onFaved: ((Int) -> Unit)? = null,
) : RecyclerView.Adapter<PhotosListAdapter.MyViewHolder>(),
    ListPreloader.PreloadModelProvider<Photo> {

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

        preloadSizeProvider.setView(myViewHolder.binding.photosListImageView)
        return myViewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPhoto = photosList[position]
        val photosListImageView = holder.binding.photosListImageView

        fullRequest.load(currentPhoto.downloadUrl).into(photosListImageView)
        holder.binding.photosListLabel.text = currentPhoto.title

        drawFavedIcon(currentPhoto.faved, holder.binding.photosListFav)
    }

    private fun drawFavedIcon(
        faved: Boolean,
        photosListFav: ImageView
    ) {
        if (faved) {
            photosListFav.setImageResource(R.drawable.ic_baseline_star_16)
        } else {
            photosListFav.setImageResource(R.drawable.ic_baseline_star_border_16)
        }
    }

    override fun getItemId(position: Int): Long {
        return photosList[position].id.hashCode().toLong()
    }

    override fun getItemCount() = photosList.size

    inner class MyViewHolder(val binding: PhotosItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }

            binding.photosListFav.setOnClickListener {
                onFaved?.invoke(adapterPosition)
                drawFavedIcon(photosList[adapterPosition].faved, binding.photosListFav)
            }
        }
    }

    override fun getPreloadItems(position: Int): MutableList<Photo> {
        return photosList.subList(position, position + 1).toMutableList()
    }

    override fun getPreloadRequestBuilder(photo: Photo): RequestBuilder<*> {
        return fullRequest.load(photo.downloadUrl)
    }
}