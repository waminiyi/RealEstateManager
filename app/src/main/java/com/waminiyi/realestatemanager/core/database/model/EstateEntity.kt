package com.waminiyi.realestatemanager.core.database.model

import androidx.room.*
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.Status
import java.util.*

/**
 * Class representing a real estate property .
 *
 * @property estateUuid The unique identifier (UUID) for the estate. Automatically generated if not provided.
 * @property type The type of the estate (e.g., APARTMENT, HOUSE, etc.).
 * @property price The price of the estate in dollars.
 * @property area The area of the estate in square meters.
 * @property description The description of the estate.
 * @property address The address details of the estate.
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
        ),
        ForeignKey(
            entity = ImageEntity::class,
            parentColumns = arrayOf("image_uuid"),
            childColumns = arrayOf("main_image_id"),
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("agent_id"), Index("main_image_id")]
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
    val address: AddressEntity,

    @ColumnInfo(name = "status")
    val status: Status,

    @ColumnInfo(name = "entry_date")
    val entryDate: Date,

    @ColumnInfo(name = "sale_date")
    val saleDate: Date? = null,

    @ColumnInfo(name = "main_image_id")
    val mainImageId: UUID,

    @ColumnInfo(name = "agent_id")
    val agentId: UUID
)
