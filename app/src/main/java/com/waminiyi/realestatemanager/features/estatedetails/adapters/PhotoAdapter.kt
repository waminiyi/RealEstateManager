package com.waminiyi.realestatemanager.features.estatedetails.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.model.data.Photo

class PhotoAdapter : ListAdapter<Photo, PhotoAdapter.PhotoViewHolder>(PhotoComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.details_photo_item, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo)
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.detailsDescriptionTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.detailsPhotoItemImageView)
        fun bind(photo: Photo) {
            textView.text = photo.description
            if (!photo.remoteUrl.isNullOrEmpty()) {
                imageView.load(photo.remoteUrl) {
                    placeholder(R.drawable.estate)
                    error(R.drawable.estate)
                }
            } else {
                imageView.load(photo.localPath) {
                    placeholder(R.drawable.estate)
                    error(R.drawable.estate)
                }
            }
        }
    }

    class PhotoComparator : DiffUtil.ItemCallback<Photo>() {

        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.uuid == newItem.uuid
                    && oldItem.remoteUrl == newItem.remoteUrl
                    && oldItem.description == newItem.description
                    && oldItem.localPath == newItem.localPath
                    && oldItem.isMain == newItem.isMain
        }
    }
}