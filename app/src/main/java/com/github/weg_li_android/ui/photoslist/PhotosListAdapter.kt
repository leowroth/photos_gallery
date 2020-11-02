package com.github.weg_li_android.ui.photoslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.weg_li_android.databinding.PhotoCellBinding

class PhotosListAdapter(
    private val urls: List<String>,
    var onItemClick: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<PhotosListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            PhotoCellBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentUrl = urls[position]
        val photosListImageView = holder.binding.photosListImageView

        Glide
            .with(holder.itemView)
            .load(currentUrl)
            .into(photosListImageView)
    }

    override fun getItemId(position: Int): Long {
        return urls[position].hashCode().toLong()
    }

    override fun getItemCount() = urls.size

    inner class MyViewHolder(val binding: PhotoCellBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }
    }
}