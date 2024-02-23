package com.waminiyi.realestatemanager.core.data.remote.model

data class RemoteEstate(
    val estateUuid: String = "",
    val type: String = "",
    val price: Int = 0,
    val area: Int = 0,
    val roomsCount: Int? = null,
    val bedroomsCount: Int? = null,
    val bathroomsCount: Int? = null,
    val description: String? = null,
    val address: RemoteAddress = RemoteAddress(),
    val status: String = "",
    val entryDate: Long? = null,
    val saleDate: Long? = null,
    val agentId: String = "",
    val poiList: List<String> = emptyList(),
)

