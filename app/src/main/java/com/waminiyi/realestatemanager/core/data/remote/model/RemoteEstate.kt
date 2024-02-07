package com.waminiyi.realestatemanager.core.data.remote.model

data class RemoteEstate(
    val estateUuid: String = "",
    val type: String = "",
    val price: Int = 0,
    val area: Int = 0,
    val roomsCount: Int = 0,
    val bedroomsCount: Int,
    val bathroomsCount: Int,
    val description: String = "",
    val address: RemoteAddress = RemoteAddress(),
    val status: String = "",
    val entryDate: Long = 0,
    val saleDate: Long? = null,
    val agentId: String = "",
    val poiList: List<String> = emptyList(),
)

