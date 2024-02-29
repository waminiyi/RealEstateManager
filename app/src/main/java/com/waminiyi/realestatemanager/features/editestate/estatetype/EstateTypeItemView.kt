package com.waminiyi.realestatemanager.features.editestate.estatetype

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.features.extensions.showAsSelected
import com.waminiyi.realestatemanager.features.model.asUiEstateType

class EstateTypeItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView
    private val textView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.estate_type_item_view, this, true)
        orientation = VERTICAL
        imageView = findViewById(R.id.itemEstateTypeImageView)
        textView = findViewById(R.id.itemEstateTypeNameTextView)
        imageView.setBackgroundResource(R.drawable.circular_border_with_transparent_background)
    }

    fun setEstateType(estateType: EstateType) {
        textView.text = estateType.asUiEstateType(context).name
        imageView.setImageResource(estateType.asUiEstateType(context).iconResId)
    }

    fun showAsSelected(isSelected: Boolean) {
        textView.showAsSelected(isSelected, R.color.cinnabar, R.color.dim_gray)
        imageView.showAsSelected(isSelected, R.color.cinnabar, R.color.dim_gray)
    }
}