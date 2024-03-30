package com.waminiyi.realestatemanager.database

import com.waminiyi.realestatemanager.core.database.model.AddressEntity
import com.waminiyi.realestatemanager.core.database.model.AgentEntity
import com.waminiyi.realestatemanager.core.database.model.EstateEntity
import com.waminiyi.realestatemanager.core.database.model.PhotoEntity
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.Location
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import java.util.Date
import java.util.UUID

object TestDataGenerator {

    private val firstNames = listOf("John", "Jane", "Alice", "Bob", "Charlie")
    private val lastNames = listOf("Doe", "Smith", "Johnson", "Williams", "Brown")
    private val adjectives = listOf("Spacious", "Cozy", "Modern", "Charming", "Luxurious")
    private val facilityAdjectives = listOf("Modern", "Fully-Equipped", "Relaxing", "Outdoor", "Secure")
    private val streetNames = listOf("Maple", "Oak", "Main", "Cedar", "Elm")
    private val cities = listOf("New York", "Los Angeles", "Chicago", "Houston", "Phoenix")
    private val states = listOf("NY", "CA", "IL", "TX", "AZ")


    fun getRandomAgent(): AgentEntity {
        val uuid = UUID.randomUUID()
        val firstName = getRandomString(firstNames)
        val lastName = getRandomString(lastNames)
        val email = "$firstName.$lastName@mail.com"
        val phoneNumber = getRandomPhoneNumber()
        return AgentEntity(uuid, firstName, lastName, email, phoneNumber, generateRandomURL())
    }

    fun getRandomImage(ownerId: UUID, isMainPhoto: Boolean): PhotoEntity {
        val uuid = UUID.randomUUID()
        return PhotoEntity(uuid, ownerId, generateRandomURL(), generateRandomLocalPath(), isMainPhoto, getRandomString(facilityAdjectives))
    }

    private fun getRandomPoi(count: Int): List<PointOfInterest> {
        val allTypes = PointOfInterest.entries.toTypedArray()
        return (1..count).map { allTypes.random() }
    }

    fun getRandomEstate(estateUuid: UUID = UUID.randomUUID(), agentUuId: UUID): EstateEntity {
        val estateStatus = EstateStatus.entries.toTypedArray().random()

        return EstateEntity(
            estateUuid = estateUuid,
            type = EstateType.entries.toTypedArray().random(),
            price = (500000..5000000).random(),
            area = getRandomInt(50, 500),
            roomsCount = (2..10).random(),
            bathroomsCount = 1,
            bedroomsCount = 1,
            description = getRandomString(adjectives),
            addressEntity = getRandomAddress(),
            estateStatus = estateStatus,
            entryDate = getRandomDate(),
            saleDate = if (estateStatus == EstateStatus.SOLD) getRandomDate() else null,
            agentId = agentUuId,
            poiList = getRandomPoi((0..8).random()),
        )
    }

    private fun getRandomString(list: List<String>): String {
        return list.random()
    }

    private fun generateRandomURL(): String {
        val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val randomString = (1..10)
            .map { chars.random() }
            .joinToString("")
        return "https://example.com/$randomString"
    }

    private fun generateRandomLocalPath(): String {
        val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val randomString = (1..10)
            .map { chars.random() }
            .joinToString("")
        return "/path/to/$randomString"
    }

    private fun getRandomAddress(): AddressEntity {
        val streetNumber = (1..100).random()
        val streetName = getRandomString(streetNames)
        val city = getRandomString(cities)
        val state = getRandomString(states)
        val postalCode = (10000..99999).random()
        val location = Location(Math.random() * 180 - 90, Math.random() * 360 - 180)

        return AddressEntity(streetNumber, streetName, city, state, "USA", postalCode, location)
    }

    private fun getRandomPhoneNumber(): String {
        return "+1-${(1000000000..9999999999).random()}"
    }

    private fun getRandomInt(min: Int, max: Int): Int {
        return min + (max - min) * Math.random().toInt()
    }

    private fun getRandomDate(): Date {
        val currentTimeMillis = System.currentTimeMillis()
        val randomMillis = (0..currentTimeMillis).random()
        return Date(randomMillis)
    }
}

