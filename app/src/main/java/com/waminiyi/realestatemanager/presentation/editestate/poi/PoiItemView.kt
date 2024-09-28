package com.waminiyi.realestatemanager.presentation.editestate.poi

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.data.models.PointOfInterest
import com.waminiyi.realestatemanager.presentation.extensions.showAsSelected
import com.waminiyi.realestatemanager.presentation.model.asUiPointOfInterest

class PoiItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView
    private val textView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.poi_item_view, this, true)
        orientation = VERTICAL
        imageView = findViewById(R.id.itemPoiImageView)
        textView = findViewById(R.id.itemPoiNameTextView)
        imageView.setBackgroundResource(R.drawable.circular_border_with_transparent_background)
    }

    fun setPoi(poi: PointOfInterest) {
        textView.text = poi.asUiPointOfInterest(context).name
        imageView.setImageResource(poi.asUiPointOfInterest(context).iconResId)
    }

    fun showAsSelected(isSelected: Boolean) {
        textView.showAsSelected(isSelected, R.color.cinnabar, R.color.dim_gray)
        imageView.showAsSelected(isSelected, R.color.cinnabar, R.color.dim_gray)
    }
}