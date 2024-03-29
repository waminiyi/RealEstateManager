package com.waminiyi.realestatemanager.data

import com.waminiyi.realestatemanager.core.data.repository.DefaultAgentRepository
import com.waminiyi.realestatemanager.core.data.repository.DefaultEstateRepository
import com.waminiyi.realestatemanager.core.database.dao.EstateDao
import com.waminiyi.realestatemanager.core.database.model.AgentEntity
import com.waminiyi.realestatemanager.core.database.model.EstateEntity
import io.mockk.clearAllMocks
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import java.util.UUID

class DefaultEstateRepositoryTest {


    private lateinit var estateDao: EstateDao
    private lateinit var repository: DefaultEstateRepository
    private lateinit var estateEntity: EstateEntity

//    @Before
//    fun setUp() {
//        estateDao = mockk()
//        repository = DefaultEstateRepository(estateDao)
//        agentEntity = AgentEntity(
//            agentUuid = UUID.fromString("51f66076-ef9b-4482-90d7-0869a472cc48"),
//            firstName = "James",
//            lastName = "Bond",
//            email = "jamesbond007@example.com",
//            phoneNumber = "0123456789",
//            photoUrl = "photourl.com",
//        )
//    }

    @After
    fun tearDown() {
        clearAllMocks()
    }
}