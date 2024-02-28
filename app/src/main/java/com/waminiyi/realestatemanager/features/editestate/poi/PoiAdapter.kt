package com.waminiyi.realestatemanager.features.editestate.poi

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest

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
        val view = PoiItemView(parent.context)
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

    class PoiViewHolder(private val poiItemView: PoiItemView) : RecyclerView.ViewHolder(poiItemView) {
        fun bind(poi: PointOfInterest, isSelected: Boolean) {
            poiItemView.setPoi(poi)
            poiItemView.showAsSelected(isSelected)
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