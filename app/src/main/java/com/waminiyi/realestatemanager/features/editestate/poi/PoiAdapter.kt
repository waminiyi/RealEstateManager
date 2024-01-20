package com.waminiyi.realestatemanager.features.editestate.poi

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import com.waminiyi.realestatemanager.features.extensions.showAsSelected
import com.waminiyi.realestatemanager.features.model.asUiEstateType
import com.waminiyi.realestatemanager.features.model.asUiPointOfInterest

class PoiAdapter(
    private val poiList: List<PointOfInterest>,
    private val onPoiSelected: (List<PointOfInterest>) -> Unit
) : ListAdapter<PointOfInterest, PoiAdapter.PoiViewHolder>(PoiComparator()) {

    private var selectedItems = listOf<PointOfInterest>()

    fun setSelectedPoiList(currentList: List<PointOfInterest>) {

        this.selectedItems = currentList
        selectedItems.forEach { poi ->
            val position = poiList.indexOf(poi)
            if (position != -1) {
                notifyItemChanged(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.poi_item, parent, false)
        return PoiViewHolder(view)
    }

    override fun onBindViewHolder(holder: PoiViewHolder, position: Int) {
        val poi = poiList[position]
        holder.bind(poi, selectedItems.contains(poi))

        holder.itemView.setOnClickListener {
            val newSelectedList = mutableListOf<PointOfInterest>()
            newSelectedList.addAll(selectedItems)
            if (newSelectedList.contains(poi)) {
                newSelectedList.remove(poi)
            } else {
                newSelectedList.add(poi)
            }
            onPoiSelected.invoke(newSelectedList)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = poiList.size

    class PoiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(poi: PointOfInterest, isSelected: Boolean) {
            val imageView: ImageView = itemView.findViewById(R.id.itemPoiImageView)
            val textView: TextView = itemView.findViewById(R.id.itemPoiNameTextView)
            textView.text = poi.asUiPointOfInterest(itemView.context).name
            imageView.setImageResource(poi.asUiPointOfInterest(itemView.context).iconResId)
            textView.showAsSelected(isSelected)
            imageView.showAsSelected(isSelected)
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