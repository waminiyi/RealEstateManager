package com.waminiyi.realestatemanager.features.editestate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waminiyi.realestatemanager.core.data.repository.EstateRepository
import com.waminiyi.realestatemanager.core.domain.usecases.AddEstateUseCase
import com.waminiyi.realestatemanager.core.model.data.Address
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.Photo
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditEstateViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val addEstateUseCase: AddEstateUseCase,
    private val estateRepository: EstateRepository
) : ViewModel() {
    private val estateId: String? = savedStateHandle["estate_id"]

    private val _uiState = MutableStateFlow(EditEstateUiState())
    val uiState: StateFlow<EditEstateUiState> = _uiState.asStateFlow()

    init {
        if (estateId != null) {
            loadEstate(estateId)
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text


    fun setType(type: EstateType) {
        _uiState.update { it.copy(type = type) }
    }

    fun setPrice(price: Int) {
        _uiState.update { it.copy(price = price) }
    }

    fun setArea(area: Float) {
        _uiState.update { it.copy(area = area) }
    }

    fun setRoomsCount(roomsCount: Int) {
        _uiState.update { it.copy(roomsCount = roomsCount) }
    }

    fun setFullDescription(fullDescription: String) {
        _uiState.update { it.copy(fullDescription = fullDescription) }
    }

    fun setMainPhoto(mainPhoto: Photo?) {
        _uiState.update { it.copy(mainPhoto = mainPhoto) }
    }

    fun setMainPhotoDescription(mainPhotoDescription: String) {
        _uiState.update { it.copy(mainPhotoDescription = mainPhotoDescription) }
    }

    fun setAdditionalPhotos(additionalPhotos: List<Photo>) {
        _uiState.update { it.copy(additionalPhotos = additionalPhotos) }
    }

    fun setAddress(address: Address?) {
        _uiState.update { it.copy(address = address) }
    }

    fun setPoiList(nearbyPointsOfInterest: List<PointOfInterest>) {
        _uiState.update { it.copy(nearbyPointsOfInterest = nearbyPointsOfInterest) }
    }

    fun setStatus(estateStatus: EstateStatus?) {
        _uiState.update { it.copy(estateStatus = estateStatus) }
    }

    fun setEntryDate(entryDate: Date?) {
        _uiState.update { it.copy(entryDate = entryDate) }
    }

    fun setSaleDate(saleDate: Date?) {
        _uiState.update { it.copy(saleDate = saleDate) }
    }

    fun setAgent(agent: Agent?) {
        _uiState.update { it.copy(agent = agent) }
    }

    fun saveEstate() {
        val hasError = !Validator().validate(_uiState.value).successful
        if (hasError) {
            _uiState.update { it.copy(savingError = "Some fields are missing") }
            return
        }

        viewModelScope.launch {

            try {
                _uiState.update { it.copy(isEstateSaving = true) }
                addEstateUseCase(
                    _uiState.value.asEstateWithDetails(estateId)
                )
                _uiState.update { it.copy(isEstateSaved = true) }

            } catch (e: Exception) {
                _uiState.update { it.copy(savingError = e.message.orEmpty()) }
            } finally {
                _uiState.update { it.copy(isEstateSaving = false) }
            }

        }
    }

    private fun loadEstate(estateId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = estateRepository.getEstateWithDetails(estateId)
            if (result !is DataResult.Success || result.data == null) {
                _uiState.update { it.copy(isLoading = false) }
            } else {
                val estate = result.data
                _uiState.update { editEstateUiState ->
                    editEstateUiState.copy(
                        isLoading = false,
                        type = estate.type,
                        price = estate.price,
                        area = estate.area,
                        roomsCount = estate.roomsCount,
                        fullDescription = estate.fullDescription,
                        // Assuming the Photo, Address, PointOfInterest, Status, Date, and Agent classes are defined
                        mainPhoto = estate.photos.first { it.isMain },
                        mainPhotoDescription = estate.photos.first { it.isMain }.description.orEmpty(),
                        additionalPhotos = estate.photos.filter { !it.isMain },
                        address = estate.address,
                        nearbyPointsOfInterest = estate.nearbyPointsOfInterest,
                        estateStatus = estate.estateStatus,
                        entryDate = estate.entryDate,
                        saleDate = estate.saleDate,
                        agent = estate.agent,
                        isEstateSaved = true,
                    )
                }
            }
        }
    }
}