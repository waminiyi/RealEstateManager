package com.waminiyi.realestatemanager.features.estateslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.model.data.Estate

class EstateListAdapter :
    ListAdapter<Estate, EstateListAdapter.EstateViewHolder>(EstateComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstateViewHolder {
        return EstateViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EstateViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class EstateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeView: TextView = itemView.findViewById(R.id.estate_type_tv)
        private val cityView: TextView = itemView.findViewById(R.id.estate_city_tv)
        private val areaView: TextView = itemView.findViewById(R.id.estate_area_tv)
        private val priceView: TextView = itemView.findViewById(R.id.estate_price_tv)

        fun bind(estate: Estate) {
            typeView.text = estate.type.name
            cityView.text = estate.addressCity
            areaView.text = estate.area.toString()
            priceView.text = estate.price.toString()
        }

        companion object {
            fun create(parent: ViewGroup): EstateViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.estate_item, parent, false)
                return EstateViewHolder(view)
            }
        }
    }

    class EstateComparator : DiffUtil.ItemCallback<Estate>() {
        override fun areItemsTheSame(oldItem: Estate, newItem: Estate): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Estate, newItem: Estate): Boolean {
            return oldItem.type == newItem.type
                    && oldItem.addressCity == newItem.addressCity
                    && oldItem.area == newItem.area
                    && oldItem.price == newItem.price
        }
    }
}