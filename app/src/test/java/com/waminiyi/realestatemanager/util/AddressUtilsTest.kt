package com.waminiyi.realestatemanager.util

import android.os.Parcel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AddressComponent
import com.google.android.libraries.places.api.model.AddressComponents
import com.google.android.libraries.places.api.model.Place
import com.waminiyi.realestatemanager.core.model.data.Address
import com.waminiyi.realestatemanager.core.model.data.Location
import com.waminiyi.realestatemanager.core.util.util.createAddressFromPlace
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddressUtilsTest {

    @Mock
    private lateinit var mockPlace: Place

    @Mock
    private lateinit var mockAddressComponents: AddressComponents

    private val latLng = LatLng(1.0, 1.0)

    @Before
    fun setup() {
        `when`(mockPlace.addressComponents).thenReturn(mockAddressComponents)
        `when`(mockPlace.latLng).thenReturn(latLng)
    }

    @Test
    fun `test createAddressFromPlace with all components present`() {
        `when`(mockAddressComponents.asList()).thenReturn(
            listOf(
                AddressComponentMock("street_number", "123"),
                AddressComponentMock("route", "Main St"),
                AddressComponentMock("locality", "City"),
                AddressComponentMock("administrative_area_level_1", "State"),
                AddressComponentMock("country", "Country"),
                AddressComponentMock("postal_code", "12345")
            )
        )

        val address = createAddressFromPlace(mockPlace)

        assertEquals(
            Address(
                123,
                "Main St",
                "City",
                "State",
                "Country",
                12345,
                Location(latLng.latitude, latLng.latitude)
            ),
            address
        )
    }

    @Test
    fun `test createAddressFromPlace with missing components`() {
        `when`(mockAddressComponents.asList()).thenReturn(
            listOf(
                AddressComponentMock("street_number", "123"),
                AddressComponentMock("locality", "City"),
                AddressComponentMock("administrative_area_level_1", "State"),
                AddressComponentMock("country", "Country"),
            )
        )

        val address = createAddressFromPlace(mockPlace)

        assertEquals(
            Address(
                123,
                "",
                "City",
                "State",
                "Country",
                null,
                Location(latLng.latitude, latLng.latitude)
            ),
            address
        )
    }
}

class AddressComponentMock(private val type: String, private val name: String?) : AddressComponent() {
    override fun getTypes(): MutableList<String> {
        return mutableListOf(type)
    }

    override fun describeContents(): Int {
        return 1
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
    }

    override fun getName(): String {
        return name.orEmpty()
    }

    override fun getShortName(): String? {
        return name
    }
}
