package com.waminiyi.realestatemanager.features.editestate.photo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.Photo
import java.util.Collections

class PhotoAdapter(
    private val onPhotoDeleted: (Photo) -> Unit,
    private val onItemMove: (startPosition: Int, endPosition: Int) -> Unit
) : ListAdapter<Photo, PhotoAdapter.PhotoViewHolder>(PhotoComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_item, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo)

        holder.deleteButton.setOnClickListener {
            onPhotoDeleted(photo)
        }
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deleteButton: ImageButton = itemView.findViewById(R.id.deletePhotoButton)
        val imageView: ImageView = itemView.findViewById(R.id.photoItemImageView)
        fun bind(photo: Photo) {
            if (photo.remoteUrl != null) {
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

    class PhotoItemTouchHelperCallback(
        private val adapter: PhotoAdapter
    ) : ItemTouchHelper.Callback() {

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            return makeMovementFlags(dragFlags, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition
            adapter.onItemMove(fromPosition, toPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

    }


}