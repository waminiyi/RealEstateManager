package com.waminiyi.realestatemanager.features

import com.waminiyi.realestatemanager.core.database.model.AddressEntity
import com.waminiyi.realestatemanager.core.database.model.AgentEntity
import com.waminiyi.realestatemanager.core.database.model.EstateEntity
import com.waminiyi.realestatemanager.core.database.model.PhotoEntity
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.Location
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID


//region agents
val agentsUuidList = listOf(
    "51f66076-ef9b-4482-90d7-0869a472cc48",
    "5b8079a7-d407-45a9-9f94-3cbac995f91e",
    "46edfbdd-2845-4bfe-a39b-60db63da8b11",
    "c8633ac9-3ec5-4d97-b0bc-5cf6efe30b69",
    "b066e42a-cae8-4c3f-9976-ee403b7c07a9",
    "6012f3b0-89ce-4e0d-96d8-b3436fb2ee88",
    "13752020-fc95-406a-9c21-3ddbd3048ef1",
    "fc28d6dd-4f67-4a41-820c-0f786f7d6387",
)

val phoneNumbers = listOf(
    "+1234567890",
    "+1987654321",
    "+1654321890",
    "+1789065432",
    "+1543210987",
    "+1876543210",
    "+1320987654",
    "+1765432109",
    "+1456789023",
    "+1890123456",
    "+1122334455",
    "+1444333222"
)

val agentsPhotoUrls = listOf(
    "https://images.pexels.com/photos/10376002/pexels-photo-10376002.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
    "https://images.pexels.com/photos/7578866/pexels-photo-7578866.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
    "https://images.pexels.com/photos/8469936/pexels-photo-8469936.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
    "https://images.pexels.com/photos/7641999/pexels-photo-7641999.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
    "https://images.pexels.com/photos/8482512/pexels-photo-8482512.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
    "https://images.pexels.com/photos/7937755/pexels-photo-7937755.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
    "https://images.pexels.com/photos/7937670/pexels-photo-7937670.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
    "https://images.pexels.com/photos/7641824/pexels-photo-7641824.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
    "https://cdn.pixabay.com/photo/2021/11/14/18/13/woman-6795378_1280.jpg",
    "https://cdn.pixabay.com/photo/2023/04/17/03/26/man-7931566_1280.jpg",
    "https://cdn.pixabay.com/photo/2017/06/26/02/47/man-2442565_1280.jpg",
    "https://cdn.pixabay.com/photo/2016/11/29/09/38/adult-1868750_1280.jpg"
)

val agentEntities = agentsUuidList.mapIndexed { index, uuid ->
    val names = getNames(index)
    AgentEntity(
        agentUuid = UUID.fromString(uuid),
        firstName = names.first,
        lastName = names.second,
        email = "${names.first.lowercase()}.${names.second.lowercase()}@example.com",
        phoneNumber = phoneNumbers[index],
        photoUrl = agentsPhotoUrls[index],
    )
}

private fun getNames(index: Int): Pair<String, String> {
    val commonFirstNames = listOf(
        "John",
        "Jane",
        "Michael",
        "Emily",
        "David",
        "Sarah",
        "Christopher",
        "Emma",
        "Matthew",
        "Olivia",
        "Daniel",
        "Sophia"
    )

    val commonLastNames = listOf(
        "Smith",
        "Johnson",
        "Williams",
        "Jones",
        "Brown",
        "Davis",
        "Miller",
        "Wilson",
        "Moore",
        "Taylor",
        "Anderson",
        "Thomas"
    )

    return commonFirstNames[index % commonFirstNames.size] to commonLastNames[index % commonLastNames.size]
}

//endRegion
// region estates
val estateUuidList = listOf(
    "f40d9e42-2f6c-4cde-be59-1818d8ec3a9e",
    "d67a9ae8-776e-4c75-bc8f-ebf64d743f73",
    "2c3bd8a8-3388-460e-af23-c6e54cf61206",
    "39bd0f42-d6a1-4968-8971-07b27b08ee95",
    "b3e2d658-1584-4c56-855b-129c6a517773",
    "78d8370c-2011-4d56-ad10-22fe59522b1e",
    "cde0ed83-ddf3-437d-860b-bdb072de582f",
    "f33fc917-9002-40c1-ae46-4751746df269",
)

val addresses = listOf(
    AddressEntity(
        69,
        "Roberts Street",
        "Granger",
        "Indiana",
        "USA",
        46530,
        Location(41.7531, -86.1111)
    ),
    AddressEntity(
        563,
        "North 8th Rd.",
        "Henrico",
        "Virginia",
        "USA",
        23228,
        Location(37.6660, -77.5167)
    ),
    AddressEntity(
        829,
        "Carriage St.",
        "Valparaiso",
        "Indiana",
        "USA",
        46383,
        Location(41.4731, -87.0611)
    ),
    AddressEntity(
        60,
        "Kent St.",
        "Birmingham",
        "Alabama",
        "USA",
        35209,
        Location(33.4942, -86.8004)
    ),
    AddressEntity(
        7996,
        "Military Court",
        "Palm Coast",
        "Florida",
        "USA",
        32137,
        Location(29.5844, -81.2078)
    ),
    AddressEntity(
        801,
        "West Miles Rd.",
        "Chillicothe",
        "Ohio",
        "USA",
        45601,
        Location(39.3331, -82.9826)
    ),
    AddressEntity(
        507,
        "West Prince St.",
        "Georgetown",
        "South Carolina",
        "USA",
        29440,
        Location(33.3696, -79.2927)
    ),
    AddressEntity(
        813,
        "Homewood Lane",
        "Bronx",
        "New York",
        "USA",
        10451,
        Location(40.8196, -73.9235)
    )
)

fun generatePoiList(index: Int): List<PointOfInterest> {
    val startIndex = index * 4
    val allPoi = PointOfInterest.entries.toTypedArray()

    return listOf(
        allPoi[startIndex % allPoi.size],
        allPoi[(startIndex + 1) % allPoi.size],
        allPoi[(startIndex + 2) % allPoi.size],
        allPoi[(startIndex + 3) % allPoi.size]
    )
}

fun generateEstateStatus(index: Int): EstateStatus {
    return if (index % 2 == 0) {
        EstateStatus.AVAILABLE
    } else {
        EstateStatus.SOLD
    }
}

fun generateDatePairs(): List<Pair<Date, Date?>> {
    val datePairs = mutableListOf<Pair<Date, Date?>>()

    val entryDates = listOf(
        "2023-12-01",
        "2024-01-01",
        "2024-02-01",
        "2024-03-01",
        "2024-03-05",
        "2024-03-07",
        "2024-03-10",
        "2024-03-14"
    )

    for (i in entryDates.indices) {
        val entryDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(entryDates[i])
        val saleDate = if (generateEstateStatus(i) == EstateStatus.SOLD) {
            // Calculate sale date as one month after entry date
            val calendar = Calendar.getInstance()
            calendar.time = entryDate as Date
            calendar.add(Calendar.MONTH, 1)
            calendar.time
        } else {
            null
        }
        datePairs.add((entryDate to saleDate) as Pair<Date, Date?>)
    }

    return datePairs
}

val estateEntities = estateUuidList.mapIndexed { index, uuid ->
    val agentId = UUID.fromString(agentsUuidList[index])
    val poiList = generatePoiList(index)
    val type = EstateType.entries.toTypedArray()[index]

    EstateEntity(
        estateUuid = UUID.fromString(uuid),
        type = type,
        price = (100000 + index * 10000),
        area = 100 + index * 10,
        roomsCount = (2 + index % 3),
        bedroomsCount = 1,
        bathroomsCount = 1,
        description = "A beautiful $type in a prime location.",
        addressEntity = addresses[index],
        estateStatus = generateEstateStatus(index),
        entryDate = generateDatePairs()[index].first,
        saleDate = generateDatePairs()[index].second,
        agentId = agentId,
        poiList = poiList
    )
}
// endregion

//region photos
private val facilityAdjectives =
    listOf(
        "Modern",
        "Fully-Equipped",
        "Relaxing",
        "Outdoor",
        "Secure",
        "Modern",
        "Fully-Equipped",
        "Relaxing",
        "Outdoor",
        "Secure"
    )

val photoUuidList = listOf(
    "6dd92d1-6af2-4b78-bcfa-d62a8aeecbc2",
    "be9aca48-9592-44e8-be42-30cbb10156d0",
    "2be56e0a-3665-4339-9b8d-fa0b40d918dd",
    "32187598-a755-4ae7-99aa-aa39b26ab82a",
    "ed40f802-f73d-4670-969d-d36a8106aae3",
    "b5d95c79-7c52-4eea-b399-c2c4ff2d54ea",
    "1b0027c2-0324-4a5b-9a24-93fc8c9066e7",
    "2075e46a-2aca-49f1-b885-d5a09678c5b3",
    "bd799830-7969-4dcb-a67f-b76c3a4cfdb4",
    "cb421d90-ad1c-47c9-ba94-88ff89b9b8f4",
    "394346d8-2d73-435d-89c3-eda2ba1ae69d",
    "a21fffe8-d7e4-45bd-a787-122902d8597d",
    "1a63b1c8-45fe-4f3d-9195-ea035e6da0c7",
    "bcfcdf43-7bb8-4de5-a3d1-a012a6cca249",
    "627dabdd-6a43-46a2-b6e6-996b2d576cd9",
    "93de4af9-9d26-4a43-9bcc-f7cd939bcfd1",
    "c209eb1c-1b44-49e8-bfa7-5220b77cc783",
    "8a591dff-8838-465e-a9f3-4276c08c0b7b",
    "25985f57-0484-4bcf-a601-ff956cd5b81f",
    "3b66e4a3-5126-4fe9-92e6-1b98a7a8f7b8",
    "5dba16d8-16ae-41aa-ab0e-94d75920befa",
    "dd89b13d-5e89-487d-8c35-37aa019c7e69",
    "d5eebc03-1a47-4ac8-8b87-37ffb29d0b82",
    "13f3e9fb-6030-4ffb-b262-2c3ab4f72d2b"
)

val mainPhotoUrlList = listOf(
    "https://images.pexels.com/photos/323780/pexels-photo-323780.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
    "https://images.pexels.com/photos/1732414/pexels-photo-1732414.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
    "https://images.pexels.com/photos/1571463/pexels-photo-1571463.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
    "https://images.pexels.com/photos/208736/pexels-photo-208736.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
    "https://images.pexels.com/photos/276554/pexels-photo-276554.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
    "https://images.pexels.com/photos/280222/pexels-photo-280222.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
    "https://images.pexels.com/photos/1396132/pexels-photo-1396132.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
    "https://images.pexels.com/photos/210617/pexels-photo-210617.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"

)

val mainPhotoEntities = estateUuidList.mapIndexed { index, estateUuid ->
    PhotoEntity(
        photoUuid = UUID.fromString(photoUuidList[index]),
        estateUuid = UUID.fromString(estateUuid),
        url = mainPhotoUrlList[index],
        localPath = "",
        isMainPhoto = true,
        description = facilityAdjectives[index]
    )
}

//endRegion
