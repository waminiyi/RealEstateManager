package com.waminiyi.realestatemanager.features.estatesListView

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waminiyi.realestatemanager.core.model.data.Estate
import com.waminiyi.realestatemanager.core.util.util.CurrencyCode

class EstateListAdapter(
    private val onEstateSelected: (String) -> Unit,
    private var currencyCode: CurrencyCode = CurrencyCode.USD
) :
    ListAdapter<Estate, EstateListAdapter.EstateViewHolder>(EstateComparator()) {

    fun updateCurrencyCode(newCurrencyCode: CurrencyCode) {
        currencyCode = newCurrencyCode
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstateViewHolder {
        val view = EstateItemView(parent.context)
        return EstateViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstateViewHolder, position: Int) {
        val estate = getItem(position)
        holder.bind(estate, currencyCode)
        holder.itemView.setOnClickListener {
            onEstateSelected(estate.uuid)
        }
    }

    class EstateViewHolder(private val estateItemView: EstateItemView) : RecyclerView.ViewHolder(estateItemView) {
        fun bind(estate: Estate, currencyCode: CurrencyCode) {
            estateItemView.bind(estate, currencyCode)
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