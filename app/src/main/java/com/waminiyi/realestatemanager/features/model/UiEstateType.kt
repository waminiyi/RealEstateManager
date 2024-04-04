package com.waminiyi.realestatemanager.features.model

import android.content.Context
import android.util.Log
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.model.EstateType

data class UiEstateType(val name: String, val iconResId: Int)

fun EstateType.asUiEstateType(context: Context): UiEstateType {

    val namesArray = context.resources.getStringArray(R.array.estate_type_names)
    val iconResIdsArray = context.resources.obtainTypedArray(R.array.estate_type_icons)

    Log.d("iconResIdsArray", iconResIdsArray.toString())

    val name = namesArray.getOrNull(this.ordinal) ?: "Unknown"
    val iconResId = iconResIdsArray.getResourceId(this.ordinal, R.drawable.ic_home_marker)

    iconResIdsArray.recycle()


    return UiEstateType(name, iconResId)
}
