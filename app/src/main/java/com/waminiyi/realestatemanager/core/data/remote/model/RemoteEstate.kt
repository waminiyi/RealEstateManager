package com.waminiyi.realestatemanager.core.data.remote.model

data class RemoteEstate(
    val estateUuid: String,
    val type: String,
    val price: Int,
    val area: Float,
    val description: String,
    val address: RemoteAddress,
    val status: String,
    val entryDate: Long,
    val saleDate: Long? = null,
    val agentId: String,
    val poiList: List<String> = emptyList(),
    val facilitiesList: List<RemoteFacility> = emptyList()
)

