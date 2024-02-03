package com.waminiyi.realestatemanager.features.editestate

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waminiyi.realestatemanager.core.Constants
import com.waminiyi.realestatemanager.core.data.repository.EstateRepository
import com.waminiyi.realestatemanager.core.data.repository.MediaFileRepository
import com.waminiyi.realestatemanager.core.data.repository.PhotoRepository
import com.waminiyi.realestatemanager.core.domain.usecases.AddEstateUseCase
import com.waminiyi.realestatemanager.core.model.data.Address
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import com.waminiyi.realestatemanager.core.model.data.Photo
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
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
    private val estateRepository: EstateRepository,
    private val photoRepository: PhotoRepository,
    private val mediaFileRepository: MediaFileRepository,
) : ViewModel() {
    private var estateId: String? = savedStateHandle[Constants.ARG_ESTATE_ID]
    private val photosToDelete = mutableListOf<String>()
    private var initialEstate: EstateWithDetails? = null

    private val _uiState = MutableStateFlow(EditEstateUiState())
    val uiState: StateFlow<EditEstateUiState> = _uiState.asStateFlow()

    init {
        estateId?.let {
            loadEstate(it)
            Log.d("estateid", it)
        }
        if (estateId == null) {
            estateId = UUID.randomUUID().toString()
        }
    }

    fun setType(type: EstateType) {
        _uiState.update { it.copy(type = type) }
    }

    fun setPrice(price: Int?) {
        _uiState.update { it.copy(price = price) }
    }

    fun setArea(area: Int?) {
        _uiState.update { it.copy(area = area) }
    }

    fun setRoomsCount(roomsCount: Int?) {
        _uiState.update { it.copy(roomsCount = roomsCount) }
    }

    fun setFullDescription(fullDescription: String) {
        _uiState.update { it.copy(fullDescription = fullDescription) }
    }

    fun setMainPhotoDescription(mainPhotoDescription: String) {
        _uiState.update { it.copy(mainPhotoDescription = mainPhotoDescription) }
    }

    fun addPhotos(addedPhotosUri: List<Uri>) {
        estateId?.let { estateId ->
            val photoMutableList = mutableListOf<Photo>()
            photoMutableList.addAll(uiState.value.photos)
            addedPhotosUri.map { uri ->
                val uuid = UUID.randomUUID().toString()
                val photo = Photo(
                    uuid = uuid,
                    estateUuid = estateId,
                    localPath = savePhotoToInternalStorage(uri, uuid).toString()
                        .also { Log.d("vmsaveduri", it) }
                )
                Log.d("uri", uri.toString())
                photoMutableList.add(photo)
            }
            _uiState.update { it.copy(photos = photoMutableList) }
        }
    }

    fun removePhoto(removedPhoto: Photo) {
        photosToDelete.add(removedPhoto.estateUuid)
        _uiState.update {
            it.copy(photos = it.photos.minus(removedPhoto))
        }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
        viewModelScope.launch {
            photoRepository.deletePhoto(removedPhoto)
            removedPhoto.localPath?.let {
                mediaFileRepository.deletePhotoFileFromInternalStorage(it)
            }
        }
    }

    fun swapPhotoItems(fromPosition: Int, toPosition: Int) {
        val photoMutableList = mutableListOf<Photo>()
        photoMutableList.addAll(uiState.value.photos)
        Collections.swap(photoMutableList, fromPosition, toPosition)
        _uiState.update { it.copy(photos = photoMutableList) }
    }

    fun setAddress(address: Address?) {
        _uiState.update { it.copy(address = address) }
    }

    fun setPoiList(nearbyPointsOfInterest: List<PointOfInterest>) {
        _uiState.update { it.copy(nearbyPointsOfInterest = nearbyPointsOfInterest) }
    }

    fun setStatus(estateStatus: EstateStatus) {
        //TODO: add a constraint so when this is set, the sale date
        //should be set an vice versa
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

    fun resetError() {
        _uiState.update { it.copy(hasSavingError = false, savingError = null) }
    }

    fun saveEstate() {
        if (!isValidState()) {
            return
        }
        if (!hasChangesComparedToInitialEstate) {
            return
        }
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isEstateSaving = true) }
                addEstateUseCase(
                    _uiState.value.asEstateWithDetails(estateId)
                        .also { Log.d("EstateTosave", it.toString()) }
                )
                _uiState.update { it.copy(isEstateSaved = true) }

            } catch (e: Exception) {
                e.printStackTrace()
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
                Log.d("estate_loaded", estate.toString())
                initialEstate = estate
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

    private val hasChangesComparedToInitialEstate: Boolean
        get() {

            return initialEstate?.let {
                val currentEstate = uiState.value.asEstateWithDetails(estateId)
                with(currentEstate) {
                    type != it.type &&
                            price != it.price &&
                            area != it.area &&
                            roomsCount != it.roomsCount &&
                            fullDescription != it.fullDescription &&
                            photos != it.photos &&
                            address != it.address &&
                            nearbyPointsOfInterest != it.nearbyPointsOfInterest &&
                            estateStatus != it.estateStatus &&
                            entryDate != it.entryDate &&
                            saleDate != it.saleDate &&
                            agent != it.agent
                }
            } ?: true
        }

    private fun isValidState(): Boolean {
        val priceResult = validatePrice(uiState.value.price)
        val areaResult = validateArea(uiState.value.area)
        val addressResult = validateAddress(uiState.value.address)
        val roomsCountResult = validateRoomsCount(uiState.value.roomsCount)
        val agentResult = validateAgent(uiState.value.agent)
        val fullDescriptionResult = validateFullDescription(uiState.value.fullDescription)
        val photoResult = validateMainPhoto(uiState.value.photos)
        val mainPhotoDescriptionResult =
            validateMainPhotoDescription(uiState.value.mainPhotoDescription)
        val entryDateResult = validateEntryDate(uiState.value.entryDate)
        val typeResult = validateType(uiState.value.type)

        val hasError = listOf(
            priceResult,
            areaResult,
            addressResult,
            roomsCountResult,
            agentResult,
            fullDescriptionResult,
            mainPhotoDescriptionResult,
            photoResult,
            entryDateResult,
            typeResult
        ).any { !it.successful }

        if (hasError) {
            _uiState.update {
                it.copy(
                    typeError = typeResult.errorMessage,
                    priceError = priceResult.errorMessage,
                    areaError = areaResult.errorMessage,
                    addressError = addressResult.errorMessage,
                    roomsCountError = roomsCountResult.errorMessage,
                    agentError = agentResult.errorMessage,
                    fullDescriptionError = fullDescriptionResult.errorMessage,
                    mainPhotoDescriptionError = mainPhotoDescriptionResult.errorMessage,
                    photosError = photoResult.errorMessage,
                    entryDateError = entryDateResult.errorMessage,
                    hasSavingError = true,
                    savingError = "Some fields are missing"
                )
            }
            return false
        } else {
            _uiState.update {
                it.copy(
                    typeError = null,
                    priceError = null,
                    areaError = null,
                    addressError = null,
                    roomsCountError = null,
                    agentError = null,
                    fullDescriptionError = null,
                    mainPhotoDescriptionError = null,
                    photosError = null,
                    entryDateError = null,
                    hasSavingError = false,
                    savingError = null
                )
            }
        }
        return true
    }

    private fun savePhotoToInternalStorage(temporaryCapturedImageUri: Uri, outputName: String): Uri? {
        var finalUri: Uri? = null
        viewModelScope.launch {
            finalUri = mediaFileRepository.savePhotoFileToInternalStorage(
                temporaryCapturedImageUri,
                outputName
            )
            Log.d("finaluri", finalUri.toString())
        }
        return finalUri
    }
}