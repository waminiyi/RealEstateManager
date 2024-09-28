package com.waminiyi.realestatemanager.presentation.estatedetails.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waminiyi.realestatemanager.data.models.PointOfInterest
import com.waminiyi.realestatemanager.presentation.editestate.poi.PoiItemView

class PoiAdapter : ListAdapter<PointOfInterest, PoiAdapter.PoiViewHolder>(PoiComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoiViewHolder {
        val view = PoiItemView(parent.context)
        return PoiViewHolder(view)
    }

    override fun onBindViewHolder(holder: PoiViewHolder, position: Int) {
        val poi = getItem(position)
        holder.bind(poi)
    }

    class PoiViewHolder(private val poiItemView: PoiItemView) : RecyclerView.ViewHolder(poiItemView) {
        fun bind(poi: PointOfInterest) {
            poiItemView.setPoi(poi)
            poiItemView.showAsSelected(true)
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