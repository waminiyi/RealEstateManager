package com.waminiyi.realestatemanager.features.model

import android.content.Context
import android.util.Log
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.model.PointOfInterest

data class UiPointOfInterest(val name: String, val iconResId: Int)

fun PointOfInterest.asUiPointOfInterest(context: Context): UiPointOfInterest {

    val namesArray = context.resources.getStringArray(R.array.poi_names)
    val iconResIdsArray = context.resources.obtainTypedArray(R.array.poi_icons)

    Log.d("iconResIdsArray", iconResIdsArray.toString())

    val name = namesArray.getOrNull(this.ordinal) ?: "Unknown"
    val iconResId = iconResIdsArray.getResourceId(this.ordinal, R.drawable.ic_poi)

    iconResIdsArray.recycle()

    return UiPointOfInterest(name, iconResId)
}
