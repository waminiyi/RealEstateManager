package com.waminiyi.realestatemanager.features.estateslist

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.model.data.Estate
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import com.waminiyi.realestatemanager.core.util.util.CurrencyCode
import com.waminiyi.realestatemanager.core.util.util.priceToText
import com.waminiyi.realestatemanager.features.model.asUiEstateType

class EstateItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val typeTextView: TextView
    private val cityTextView: TextView
    private val areaTextView: TextView
    private val priceTextView: TextView
    private val roomCountTextView: TextView
    private val statusTextView: TextView
    private val imageView: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.estate_item_view, this, true)
        typeTextView = this.findViewById(R.id.estate_type_tv)
        cityTextView = this.findViewById(R.id.estate_city_tv)
        areaTextView = this.findViewById(R.id.estate_area_tv)
        priceTextView = this.findViewById(R.id.estate_price_tv)
        roomCountTextView = this.findViewById(R.id.itemRoomsCountTextView)
        statusTextView = this.findViewById(R.id.itemEstateStatusTextView)
        imageView = this.findViewById(R.id.estateItemImageView)
    }

    fun bind(estate: Estate, currencyCode: CurrencyCode) {
        typeTextView.text = estate.type.asUiEstateType(context).name
        cityTextView.text = estate.addressCity
        areaTextView.text = context.getString(R.string.areaInSquareMeter, estate.area)
        priceTextView.text = priceToText(estate.price, currencyCode)

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

        with(statusTextView) {
            when (estate.status) {
                EstateStatus.AVAILABLE -> {
                    text = context.getString(R.string.available)
                    setTextColor(ContextCompat.getColor(context, R.color.green))
                }

                EstateStatus.SOLD -> {
                    text = context.getString(R.string.sold)
                    setTextColor(ContextCompat.getColor(context, R.color.red))
                }
            }
        }

        estate.roomsCount?.let {
            this.findViewById<ImageView>(R.id.dot).visibility = View.VISIBLE
            roomCountTextView.text = context.resources.getQuantityString(R.plurals.roomsCount, it, it)
        } ?: {
            this.findViewById<ImageView>(R.id.dot).visibility = View.GONE
        }
    }
}
