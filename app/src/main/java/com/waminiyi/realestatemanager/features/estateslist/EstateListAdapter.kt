package com.waminiyi.realestatemanager.features.estateslist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.model.data.Estate
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import com.waminiyi.realestatemanager.core.util.util.CurrencyCode
import com.waminiyi.realestatemanager.core.util.util.priceToText
import com.waminiyi.realestatemanager.features.model.asUiEstateType

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
        return EstateViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EstateViewHolder, position: Int) {
        val estate = getItem(position)
        holder.bind(estate, currencyCode)
        holder.itemView.setOnClickListener {
            onEstateSelected(estate.uuid)
        }
    }

    class EstateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeView: TextView = itemView.findViewById(R.id.estate_type_tv)
        private val cityView: TextView = itemView.findViewById(R.id.estate_city_tv)
        private val areaView: TextView = itemView.findViewById(R.id.estate_area_tv)
        private val priceView: TextView = itemView.findViewById(R.id.estate_price_tv)
        private val roomCountTextView: TextView = itemView.findViewById(R.id.itemRoomsCountTextView)
        private val statusTextView: TextView = itemView.findViewById(R.id.itemEstateStatusTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.estateItemImageView)

        fun bind(estate: Estate, currencyCode: CurrencyCode) {
            typeView.text = estate.type.asUiEstateType(itemView.context).name
            cityView.text = estate.addressCity
            areaView.text = itemView.context.getString(R.string.areaInSquareMeter, estate.area)
            priceView.text = priceToText(estate.price, currencyCode)

            if (!estate.mainPhoto.remoteUrl.isNullOrBlank()) {
                imageView.load(estate.mainPhoto.remoteUrl) {
                    placeholder(R.drawable.estate)
                    error(R.drawable.estate)
                }
            } else {
                imageView.load(estate.mainPhoto.localPath) {
                    placeholder(R.drawable.estate)
                    error(R.drawable.estate)
                }
            }
            Log.d("Item", estate.toString())

            with(statusTextView) {
                when (estate.status) {
                    EstateStatus.AVAILABLE -> {
                        text = this.context.getString(R.string.available)
                        setTextColor(ContextCompat.getColor(context, R.color.green))
                    }

                    EstateStatus.SOLD -> {
                        text = this.context.getString(R.string.sold)
                        setTextColor(ContextCompat.getColor(context, R.color.red))
                    }
                }
            }

//            estate.roomsCount?.let {
//                roomCountTextView.text=
//            }
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