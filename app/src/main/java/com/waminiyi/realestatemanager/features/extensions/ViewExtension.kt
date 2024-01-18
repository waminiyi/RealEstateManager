package com.waminiyi.realestatemanager.features.extensions

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.features.model.asUiEstateType

fun TextView.showAsSelected(isSelected: Boolean) {
    val textColor = if (isSelected) {
        R.color.selectedItemColor
    } else {
        R.color.nonSelectedItemColor
    }

    setTextColor(ContextCompat.getColor(context, textColor))
}

fun ImageView.showAsSelected(isSelected: Boolean) {
    val backgroundRes = if (isSelected) {
        R.drawable.selected_rounded_border
    } else {
        R.drawable.non_selected_rounded_border
    }
    setBackgroundResource(backgroundRes)
    val color = if (isSelected) {
        R.color.selectedItemColor
    } else {
        R.color.nonSelectedItemColor
    }
    drawable.setTint(ContextCompat.getColor(context, color))

}

fun TextView.showAsEstateType(estateType: EstateType) {
    Log.d("UiEstateType", estateType.asUiEstateType(context).toString())

    val estateTypeIcon =
        ContextCompat.getDrawable(context, estateType.asUiEstateType(context).iconResId)

    this.setCompoundDrawablesWithIntrinsicBounds(null, estateTypeIcon, null, null)
    this.text = estateType.asUiEstateType(context).name
}