package com.waminiyi.realestatemanager.features.editestate

import android.net.Uri
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
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditEstateViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val addEstateUseCase: AddEstateUseCase,
    private val estateRepository: EstateRepository
) : ViewModel() {
    private var estateId: String? = savedStateHandle["estate_id"]
    private val photosToDelete = mutableListOf<String>()
    private var currentEstate: EstateWithDetails? = null

    private val _uiState = MutableStateFlow(EditEstateUiState())
    val uiState: StateFlow<EditEstateUiState> = _uiState.asStateFlow()

    init {
        estateId?.let { loadEstate(it) }
        if (estateId == null) {
            estateId = UUID.randomUUID().toString()
        }
    }

    fun setType(type: EstateType) {
        _uiState.update { it.copy(type = type) }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
    }

    fun setPrice(price: Int?) {
        _uiState.update { it.copy(price = price) }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
    }

    fun setArea(area: Float?) {
        _uiState.update { it.copy(area = area) }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
    }

    fun setRoomsCount(roomsCount: Int?) {
        _uiState.update { it.copy(roomsCount = roomsCount) }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
    }

    fun setFullDescription(fullDescription: String) {
        _uiState.update { it.copy(fullDescription = fullDescription) }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
    }

    fun setAsMainPhoto(photo: Photo?) {
        _uiState.update {
            //TODO: add logic for changing the booleans isMain value and photo description
            val photoMutableList = mutableListOf<Photo>()
            photoMutableList.addAll(it.photos)
            photoMutableList.swap(0, it.photos.indexOf(photo))
            it.copy(photos = photoMutableList)
        }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
    }

    fun setMainPhotoDescription(mainPhotoDescription: String) {
        _uiState.update { it.copy(mainPhotoDescription = mainPhotoDescription) }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
    }

    fun addPhotos(addedPhotosUri: List<Uri>) {
        estateId?.let { estateId ->
            val photoMutableList = mutableListOf<Photo>()
            photoMutableList.addAll(uiState.value.photos)
            addedPhotosUri.map {
                val photo = Photo(
                    uuid = UUID.randomUUID().toString(),
                    estateUuid = estateId,
                    localPath = it.toString()
                )
                photoMutableList.add(photo)
            }
            _uiState.update { it.copy(photos = photoMutableList) }
            _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
        }
    }

    fun removePhoto(removedPhoto: Photo) {
        photosToDelete.add(removedPhoto.estateUuid)
        _uiState.update {
            it.copy(photos = it.photos.minus(removedPhoto))
        }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
    }

    fun swapPhotoItems(fromPosition: Int, toPosition: Int) {
        val photoMutableList = mutableListOf<Photo>()
        photoMutableList.addAll(uiState.value.photos)
        Collections.swap(photoMutableList, fromPosition, toPosition)
        _uiState.update { it.copy(photos = photoMutableList) }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
    }

    fun setAddress(address: Address?) {
        _uiState.update { it.copy(address = address) }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
    }

    fun setPoiList(nearbyPointsOfInterest: List<PointOfInterest>) {
        _uiState.update { it.copy(nearbyPointsOfInterest = nearbyPointsOfInterest) }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
    }

    fun setStatus(estateStatus: EstateStatus) {
        _uiState.update { it.copy(estateStatus = estateStatus) }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
    }

    fun setEntryDate(entryDate: Date?) {
        _uiState.update { it.copy(entryDate = entryDate) }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
    }

    fun setSaleDate(saleDate: Date?) {
        _uiState.update { it.copy(saleDate = saleDate) }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
    }

    fun setAgent(agent: Agent?) {
        _uiState.update { it.copy(agent = agent) }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
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
                currentEstate = estate
                _uiState.update { editEstateUiState ->
                    editEstateUiState.copy(
                        isLoading = false,
                        type = estate.type,
                        price = estate.price,
                        area = estate.area,
                        roomsCount = estate.roomsCount,
                        fullDescription = estate.fullDescription,
                        mainPhotoDescription = estate.photos.first { it.isMain }.description.orEmpty(),
                        photos = estate.photos,
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

    fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }

    private val hasChangesComparedToInitialEstate: Boolean
        get() {

            return currentEstate?.let { currentEstate ->
                uiState.value.type != currentEstate.type &&
                        uiState.value.price != currentEstate.price &&
                        uiState.value.area != currentEstate.area &&
                        uiState.value.roomsCount != currentEstate.roomsCount &&
                        uiState.value.fullDescription != currentEstate.fullDescription &&
                        uiState.value.mainPhotoDescription != currentEstate.photos.first { it.isMain }.description.orEmpty() &&
                        uiState.value.photos != currentEstate.photos &&
                        uiState.value.address != currentEstate.address &&
                        uiState.value.nearbyPointsOfInterest != currentEstate.nearbyPointsOfInterest &&
                        uiState.value.estateStatus != currentEstate.estateStatus &&
                        uiState.value.entryDate != currentEstate.entryDate &&
                        uiState.value.saleDate != currentEstate.saleDate &&
                        uiState.value.agent != currentEstate.agent
            } ?: uiState.value.isNotDefaultState()
        }
}