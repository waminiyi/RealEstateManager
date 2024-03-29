package com.waminiyi.realestatemanager.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.waminiyi.realestatemanager.core.Constants.ESTATES_TABLE_NAME
import com.waminiyi.realestatemanager.core.model.data.Estate
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import com.waminiyi.realestatemanager.core.model.data.Photo
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import java.util.Date
import java.util.UUID

/**
 * Class representing a real estate property .
 *
 * @property estateUuid The unique identifier (UUID) for the estate. Automatically generated if not provided.
 * @property type The type of the estate (e.g., APARTMENT, HOUSE, etc.).
 * @property price The price of the estate in dollars.
 * @property area The area of the estate in square meters.
 * @property description The description of the estate.
 * @property addressEntity The address details of the estate.
 * @property estateStatus The status of the estate (AVAILABLE, SOLD, etc.).
 * @property entryDate The date when the estate was listed.
 * @property saleDate The date when the estate was sold (can be null if not sold yet).
 * @property agentId The unique identifier (UUID) of the agent associated with the estate.
 */
@Entity(
    tableName = ESTATES_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = AgentEntity::class,
            parentColumns = arrayOf("agent_uuid"),
            childColumns = arrayOf("agent_id"),
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("agent_id")]
)
data class EstateEntity(
    @PrimaryKey
    @ColumnInfo(name = "estate_uuid")
    val estateUuid: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "type")
    val type: EstateType,

    @ColumnInfo(name = "price")
    val price: Int,

    @ColumnInfo(name = "area")
    val area: Int,

    @ColumnInfo(name = "rooms_count")
    val roomsCount: Int?,

    @ColumnInfo(name = "bedrooms_count")
    val bedroomsCount: Int?,

    @ColumnInfo(name = "bathrooms_count")
    val bathroomsCount: Int?,

    @ColumnInfo(name = "description")
    val description: String?,

    @Embedded
    val addressEntity: AddressEntity,

    @ColumnInfo(name = "status")
    val estateStatus: EstateStatus,

    @ColumnInfo(name = "entry_date")
    val entryDate: Date?,

    @ColumnInfo(name = "sale_date")
    val saleDate: Date? = null,

    @ColumnInfo(name = "agent_id")
    val agentId: UUID,

    @ColumnInfo(name = "poi_list")
    val poiList: List<PointOfInterest> = emptyList()

) {
    fun asEstate(photo: Photo) = Estate(
        uuid = this.estateUuid.toString(),
        type = this.type,
        price = this.price,
        area = this.area,
        mainPhoto = photo,
        addressCity = this.addressEntity.city,
        location = this.addressEntity.location,
        status = this.estateStatus,
        roomsCount = this.roomsCount
    )
}

fun EstateWithDetails.asEstateEntity() = EstateEntity(
    estateUuid = UUID.fromString(this.uuid),
    type = this.type,
    price = this.price,
    area = this.area,
    roomsCount = this.roomsCount,
    bedroomsCount = this.bedroomsCount,
    bathroomsCount = this.bathroomsCount,
    description = this.fullDescription,
    addressEntity = this.address.asAddressEntity(),
    estateStatus = this.estateStatus,
    entryDate = this.entryDate,
    saleDate = this.saleDate,
    agentId = UUID.fromString(this.agent.uuid),
    poiList = this.nearbyPointsOfInterest,
)

fun Map.Entry<EstateEntity, List<PhotoEntity>>.asEstate() =
    this.key.asEstate(this.value.first().asPhoto())
