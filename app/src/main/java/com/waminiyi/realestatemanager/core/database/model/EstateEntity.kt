package com.waminiyi.realestatemanager.core.database.model

import androidx.room.*
import com.waminiyi.realestatemanager.core.model.data.*
import java.util.*

/**
 * Class representing a real estate property .
 *
 * @property estateUuid The unique identifier (UUID) for the estate. Automatically generated if not provided.
 * @property type The type of the estate (e.g., APARTMENT, HOUSE, etc.).
 * @property price The price of the estate in dollars.
 * @property area The area of the estate in square meters.
 * @property description The description of the estate.
 * @property addressEntity The address details of the estate.
 * @property status The status of the estate (AVAILABLE, SOLD, etc.).
 * @property entryDate The date when the estate was listed.
 * @property saleDate The date when the estate was sold (can be null if not sold yet).
 * @property mainImageId The unique identifier (UUID) of the main image associated with the estate.
 * @property agentId The unique identifier (UUID) of the agent associated with the estate.
 */
@Entity(
    tableName = "estates",
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
    val area: Float,

    @ColumnInfo(name = "description")
    val description: String,

    @Embedded
    val addressEntity: AddressEntity,

    @ColumnInfo(name = "status")
    val status: Status,

    @ColumnInfo(name = "entry_date")
    val entryDate: Date,

    @ColumnInfo(name = "sale_date")
    val saleDate: Date? = null,

    @ColumnInfo(name = "agent_id")
    val agentId: UUID,

    @ColumnInfo(name = "poi_list")
    val poiList: List<PointOfInterest> = emptyList(),

    @ColumnInfo(name = "facilities_list")
    val facilitiesList: List<Facility> = emptyList(),

    ) {
    fun asEstate(photo: Photo) = Estate(
        uuid = this.estateUuid.toString(),
        type = this.type,
        price = this.price,
        area = this.area,
        mainPhoto = photo,
        address = this.addressEntity.asAddress(),
        status = this.status
    )
}

fun EstateWithDetails.asEstateEntity() = EstateEntity(
    estateUuid = UUID.fromString(this.uuid),
    type = this.type,
    price = this.price,
    area = this.area,
    description = this.description,
    addressEntity = this.address.asAddressEntity(),
    status = this.status,
    entryDate = this.entryDate,
    saleDate = this.saleDate,
    agentId = UUID.fromString(this.agentId),
    poiList = this.nearbyPointsOfInterest,
    facilitiesList = this.facilities
)

fun Map.Entry<EstateEntity, List<PhotoEntity>>.asEstate() = this.key.asEstate(this.value.first().asPhoto())
