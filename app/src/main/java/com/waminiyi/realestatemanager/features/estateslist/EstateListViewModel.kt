package com.waminiyi.realestatemanager.features.estateslist

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waminiyi.realestatemanager.core.data.repository.EstateRepository
import com.waminiyi.realestatemanager.core.domain.usecases.AddEstateUseCase
import com.waminiyi.realestatemanager.core.model.data.Address
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import com.waminiyi.realestatemanager.core.model.data.Location
import com.waminiyi.realestatemanager.core.model.data.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.time.Instant
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EstateListViewModel @Inject constructor(
   private val estateRepository: EstateRepository,
    private val addEstateUseCase: AddEstateUseCase
) : ViewModel() {
    //How many things are we waiting for to load?
    private val numLoadingItems = MutableStateFlow(0)


    private val estates = estateRepository.getAllEstatesStream()

    val uiState = estates.map { estates ->
        when (estates) {
            is DataResult.Error -> EstateListUiState(isError = true, errorMessage = estates.exception.message?:"ERrooo")
            is DataResult.Loading -> EstateListUiState(isLoading = true)
            is DataResult.Success -> EstateListUiState(estates = estates.data)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EstateListUiState(isLoading = true)
    )

    /*fun addSampleEstates() {
        viewModelScope.launch {
            withLoading {
                val estates = arrayOf(
                    EstateWithDetails(
                        "d67a9ae8-776e-4c75-bc8f-ebf64d743f73",
                        EstateType.APARTMENT,
                        200000,
                        70.0F,
                        emptyList(),
                        "description",
                        emptyList(),
                        Address(12, "name", "city", "state", 1234, Location(1200, 2000)),
                        emptyList(),
                        Status.AVAILABLE,
                        Date(System.currentTimeMillis()),
                        null,
                        "2c3bd8a8-3388-460e-af23-c6e54cf61206"
                    ),

                    )
                estates.forEach { addEstateUseCase(it) }
            }
        }
    }*/

}