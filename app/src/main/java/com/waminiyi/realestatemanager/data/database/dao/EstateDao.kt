package com.waminiyi.realestatemanager.data.database.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.waminiyi.realestatemanager.data.database.model.EstateEntity
import com.waminiyi.realestatemanager.data.database.model.EstateWithDetailsEntity
import com.waminiyi.realestatemanager.data.database.model.PhotoEntity
import com.waminiyi.realestatemanager.data.models.EstateStatus
import com.waminiyi.realestatemanager.data.models.EstateType
import kotlinx.coroutines.flow.Flow
import java.util.Date
import java.util.UUID
import javax.inject.Singleton

@Singleton
@Dao
interface EstateDao {
    @Upsert
    suspend fun upsertEstate(estateEntity: EstateEntity)

    @Transaction
    @Query("SELECT * FROM estates WHERE estate_uuid = :estateUuid")
    suspend fun getEstateWithDetailsById(estateUuid: UUID): EstateWithDetailsEntity?


    @Transaction
    @Query(
        "SELECT * FROM estates" +
                " LEFT JOIN photos ON estates.estate_uuid = photos.estate_uuid" +
                " WHERE is_main_photo = 1" +
                " AND (:minPrice IS NULL OR price >= :minPrice)" +
                " AND (:maxPrice IS NULL OR price <= :maxPrice)" +
                " AND (:minArea IS NULL OR area >= :minArea)" +
                " AND (:maxArea IS NULL OR area <= :maxArea)" +
                " AND (:typesIsEmpty = 1 OR type IN (:types))" +
                " AND (:citiesIsEmpty = 1 OR (city IN (:cities)))" +
                " AND (:estateStatus IS NULL OR status = :estateStatus)" +
                " AND (:roomsIsEmpty = 1 OR (rooms_count IN (:roomsCounts) OR rooms_count >= :roomsCountThreshold))" +
                " AND (:bedroomsIsEmpty = 1 OR (bedrooms_count IN (:bedroomsCounts) OR bedrooms_count >= :bedroomsCountThreshold))" +
                " AND (:addedAfter IS NULL OR entry_date >= :addedAfter)" +
                " AND (:soldAfter IS NULL OR sale_date >= :soldAfter)" +
                "AND (:poi IS NULL OR (poi_list LIKE '%' || :poi || '%')) " +
                " GROUP BY estates.estate_uuid" +
                " HAVING (:photoMinimumCount IS NULL OR (SELECT COUNT(*) FROM photos WHERE photos.estate_uuid = estates.estate_uuid) >= :photoMinimumCount);"
    )
    fun getAllEstatesWithImages(
        minPrice: Int? = null,
        maxPrice: Int? = null,
        minArea: Int? = null,
        maxArea: Int? = null,
        typesIsEmpty: Boolean = true,
        types: List<EstateType> = emptyList(),
        citiesIsEmpty: Boolean = true,
        cities: List<String>? = null,
        estateStatus: EstateStatus? = null,
        roomsIsEmpty: Boolean = true,
        roomsCounts: List<Int> = emptyList(),
        roomsCountThreshold: Int? = null,
        bedroomsIsEmpty: Boolean = true,
        bedroomsCounts: List<Int> = emptyList(),
        bedroomsCountThreshold: Int? = null,
        photoMinimumCount: Int = 1,
        addedAfter: Date? = null,
        soldAfter: Date? = null,
        poi: String? = null
    ): Flow<Map<EstateEntity, List<PhotoEntity>>>

    @Transaction
    @Query("SELECT * FROM estates WHERE estate_uuid IN (:estateUuids)")
    fun getEstatesByIds(estateUuids: List<UUID>): List<EstateEntity>

    @Query("SELECT * FROM estates ")
    fun getAllEstatesWithCursor(): Cursor?

    @Transaction
    @Query("SELECT * FROM estates WHERE estate_uuid = :estateUuid")
    fun getEstateWithDetailsCursorById(estateUuid: UUID): Cursor?
}
