package com.waminiyi.realestatemanager.features.estatedetails.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import com.waminiyi.realestatemanager.features.extensions.showAsSelected
import com.waminiyi.realestatemanager.features.model.asUiPointOfInterest

class PoiAdapter : ListAdapter<PointOfInterest, PoiAdapter.PoiViewHolder>(PoiComparator()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.poi_item, parent, false)
        return PoiViewHolder(view)
    }

    override fun onBindViewHolder(holder: PoiViewHolder, position: Int) {
        val poi = getItem(position)
        holder.bind(poi)
    }

    class PoiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(poi: PointOfInterest) {
            val imageView: ImageView = itemView.findViewById(R.id.itemPoiImageView)
            val textView: TextView = itemView.findViewById(R.id.itemPoiNameTextView)
            textView.text = poi.asUiPointOfInterest(itemView.context).name
            imageView.setImageResource(poi.asUiPointOfInterest(itemView.context).iconResId)
            textView.showAsSelected(true)
            imageView.showAsSelected(true)
        }
    }

    class PoiComparator : DiffUtil.ItemCallback<PointOfInterest>() {

        override fun areItemsTheSame(oldItem: PointOfInterest, newItem: PointOfInterest): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: PointOfInterest,
            newItem: PointOfInterest
        ): Boolean {
            return oldItem.name == newItem.name
        }
    }
}