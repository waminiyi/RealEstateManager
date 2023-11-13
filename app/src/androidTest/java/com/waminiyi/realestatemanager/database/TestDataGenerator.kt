package com.waminiyi.realestatemanager.database

import com.waminiyi.realestatemanager.core.database.model.*
import com.waminiyi.realestatemanager.core.database.model.Address
import com.waminiyi.realestatemanager.core.database.model.Location
import com.waminiyi.realestatemanager.core.model.data.*
import java.util.*

object TestDataGenerator {

    private val firstNames = listOf("John", "Jane", "Alice", "Bob", "Charlie")
    private val lastNames = listOf("Doe", "Smith", "Johnson", "Williams", "Brown")
    private val facilityNames = listOf("Swimming Pool", "Gym", "Kitchen", "Garden", "Bedroom", "Bathroom")
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
        return AgentEntity(uuid, firstName, lastName, email, phoneNumber)
    }

    fun getRandomFacilities(count: Int): List<Facility> {
        return (1..count).map {
            Facility(
                0,
                type = FacilityType.values().random(),
                count = (1..3).random()
            )
        }
    }

    fun getRandomImage(ownerId: UUID, type: ImageType): ImageEntity {
        val uuid = UUID.randomUUID()
        return ImageEntity(uuid, ownerId, getRandomString(facilityNames), getRandomString(facilityAdjectives), type)
    }

    private fun getRandomPoi(count: Int): List<PointOfInterest> {
        val allTypes = PointOfInterest.values()
        return (1..count).map { allTypes.random() }
    }

    fun getRandomEstate(estateUuid: UUID = UUID.randomUUID(), imageUuid: UUID, agentUuId: UUID): EstateEntity {
        val status = Status.values().random()

        return EstateEntity(
            estateUuid = estateUuid,
            type = EstateType.values().random(),
            price = (500000..5000000).random(),
            area = getRandomFloat(50f, 500f),
            description = getRandomString(adjectives),
            address = getRandomAddress(),
            status = status,
            entryDate = getRandomDate(),
            saleDate = if (status == Status.SOLD) getRandomDate() else null,
            mainImageId = imageUuid,
            agentId = agentUuId,
            poiList = getRandomPoi((0..8).random()),
            facilitiesList = getRandomFacilities((0..8).random())
        )
    }

    private fun getRandomString(list: List<String>): String {
        return list.random()
    }

    private fun getRandomAddress(): Address {
        val streetNumber = (1..100).random()
        val streetName = getRandomString(streetNames)
        val city = getRandomString(cities)
        val state = getRandomString(states)
        val postalCode = (10000..99999).random()
        val location = Location(Math.random() * 180 - 90, Math.random() * 360 - 180)

        return Address(streetNumber, streetName, city, state, postalCode, location)
    }

    private fun getRandomPhoneNumber(): String {
        return "+1-${(1000000000..9999999999).random()}"
    }

    private fun getRandomFloat(min: Float, max: Float): Float {
        return min + (max - min) * Math.random().toFloat()
    }

    private fun getRandomDate(): Date {
        val currentTimeMillis = System.currentTimeMillis()
        val randomMillis = (0..currentTimeMillis).random()
        return Date(randomMillis)
    }
}

