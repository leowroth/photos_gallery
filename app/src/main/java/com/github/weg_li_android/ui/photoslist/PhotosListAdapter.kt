package com.github.weg_li_android.ui.photoslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.weg_li_android.R

class PhotosListAdapter(
    private val urls: List<String>,
    private val inflater: LayoutInflater,
    val clickListener: PhotosClickListener
) : RecyclerView.Adapter<PhotosListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val imageView = inflater.inflate(R.layout.photo_cell, parent, false) as ImageView
        return MyViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(holder.itemView).load(urls[position]).into(holder.imageView)
    }

    override fun getItemCount() = urls.size

    class MyViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView),
        View.OnClickListener {
        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }

    }

    interface PhotosClickListener {
        fun onPhotoClicked(imageView: ImageView, position: Int)
    }
}