package com.waminiyi.realestatemanager.presentation.editestate

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waminiyi.realestatemanager.Constants
import com.waminiyi.realestatemanager.data.repository.AgentRepository
import com.waminiyi.realestatemanager.data.repository.EstateRepository
import com.waminiyi.realestatemanager.data.repository.MediaFileRepository
import com.waminiyi.realestatemanager.data.repository.PhotoRepository
import com.waminiyi.realestatemanager.domain.usecases.AddEstateUseCase
import com.waminiyi.realestatemanager.data.models.Address
import com.waminiyi.realestatemanager.data.models.Agent
import com.waminiyi.realestatemanager.data.models.Result
import com.waminiyi.realestatemanager.data.models.EstateStatus
import com.waminiyi.realestatemanager.data.models.EstateType
import com.waminiyi.realestatemanager.data.models.EstateWithDetails
import com.waminiyi.realestatemanager.data.models.Photo
import com.waminiyi.realestatemanager.data.models.PointOfInterest
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
    savedStateHandle: SavedStateHandle,
    private val addEstateUseCase: AddEstateUseCase,
    private val estateRepository: EstateRepository,
    private val agentRepository: AgentRepository,
    private val photoRepository: PhotoRepository,
    private val mediaFileRepository: MediaFileRepository,
) : ViewModel() {
    private var estateId: String? = savedStateHandle[Constants.ARG_ESTATE_ID]
    private val photosToDelete = mutableListOf<Photo>()
    private var initialEstate: EstateWithDetails? = null

    private val _uiState = MutableStateFlow(EditEstateUiState())
    val uiState: StateFlow<EditEstateUiState> = _uiState.asStateFlow()

    init {
        loadAgents()

        estateId?.let {
            loadEstate(it)
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

    fun setBedroomsCount(bedroomsCount: Int?) {
        _uiState.update { it.copy(bedroomsCount = bedroomsCount) }
    }

    fun setBathroomsCount(bathroomsCount: Int?) {
        _uiState.update { it.copy(bathroomsCount = bathroomsCount) }
    }

    fun setFullDescription(fullDescription: String) {
        _uiState.update { it.copy(fullDescription = fullDescription) }
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
                )
                photoMutableList.add(photo)
            }
            _uiState.update { it.copy(photos = photoMutableList) }
        }
    }

    fun addPhotoToDeletionList(removedPhoto: Photo) {
        photosToDelete.add(removedPhoto)
        _uiState.update {
            it.copy(photos = it.photos.minus(removedPhoto))
        }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
    }

    fun swapPhotoItems(fromPosition: Int, toPosition: Int) {
        val photoMutableList = mutableListOf<Photo>()
        photoMutableList.addAll(uiState.value.photos)
        Collections.swap(photoMutableList, fromPosition, toPosition)
        if (fromPosition == 0) {
            photoMutableList[fromPosition] = photoMutableList[fromPosition].copy(isMain = true)
            photoMutableList[toPosition] = photoMutableList[toPosition].copy(isMain = false)
        } else if (toPosition == 0) {
            photoMutableList[toPosition] = photoMutableList[toPosition].copy(isMain = true)
            photoMutableList[fromPosition] = photoMutableList[fromPosition].copy(isMain = false)
        }
        _uiState.update { it.copy(photos = photoMutableList) }
    }

    fun updatePhotoDescription(index: Int, newDescription: String?) {
        val photoMutableList = mutableListOf<Photo>()
        photoMutableList.addAll(uiState.value.photos)
        photoMutableList[index] = photoMutableList[index].copy(description = newDescription)
        _uiState.update {
            it.copy(photos = photoMutableList)
        }
        _uiState.update { it.copy(hasChanges = hasChangesComparedToInitialEstate) }
    }

    fun setAddress(address: Address?) {
        _uiState.update { it.copy(address = address) }
    }

    fun setPoiList(nearbyPointsOfInterest: List<PointOfInterest>) {
        _uiState.update { it.copy(nearbyPointsOfInterest = nearbyPointsOfInterest) }
    }

    fun setStatus(estateStatus: EstateStatus) {
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
        _uiState.update { it.copy(savingError = null) }
    }

    private fun loadAgents() {
        viewModelScope.launch {
            when (val result = agentRepository.getAllAgents()) {
                is Result.Success -> {
                    _uiState.update { it.copy(agentsList = result.data) }
                }

                else -> _uiState.update { it.copy(agentsList = emptyList()) }
            }
        }
    }

    fun resetSavingStatus() {
        _uiState.update { it.copy(isEstateSaved = false) }
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
                        .also { Log.d("EstateTo save", it.toString()) }
                )
                photosToDelete.forEach { photo ->
                    photoRepository.deletePhoto(photo)
                    photo.localPath?.let {
                        mediaFileRepository.deletePhotoFileFromInternalStorage(it)
                    }
                }
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
            if (result !is Result.Success || result.data == null) {
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
                        bedroomsCount = estate.bedroomsCount,
                        bathroomsCount = estate.bathroomsCount,
                        fullDescription = estate.fullDescription,
                        photos = estate.photos,
                        address = estate.address,
                        nearbyPointsOfInterest = estate.nearbyPointsOfInterest,
                        estateStatus = estate.estateStatus,
                        entryDate = estate.entryDate,
                        saleDate = estate.saleDate,
                        agent = estate.agent,
                    )
                }
            }
        }
    }

    private val hasChangesComparedToInitialEstate: Boolean
        get() {
            if (uiState.value.photos.isNotEmpty()) return true
            return initialEstate?.let {
                val currentEstate = uiState.value.asEstateWithDetails(estateId)
                with(currentEstate) {
                    type != it.type &&
                            price != it.price &&
                            area != it.area &&
                            roomsCount != it.roomsCount &&
                            bedroomsCount != it.bedroomsCount &&
                            bathroomsCount != it.bathroomsCount &&
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
        val agentResult = validateAgent(uiState.value.agent)
        val photoResult = validatePhotos(uiState.value.photos)
        val datesResult = validateDates(uiState.value.entryDate, uiState.value.saleDate)
        val statusResult = validateStatus(uiState.value.estateStatus, uiState.value.saleDate)
        val typeResult = validateType(uiState.value.type)
        val roomsCountResult = uiState.value.roomsCount?.let {
            uiState.value.bedroomsCount?.let { it1 ->
                validateRoomsCount(it, it1)
            }
        }

        val hasError = listOf(
            priceResult,
            areaResult,
            addressResult,
            agentResult,
            photoResult,
            datesResult,
            statusResult,
            typeResult
        ).any { !it.successful }
        val hasRoomsCountError = roomsCountResult != null && !roomsCountResult.successful

        if (hasError || hasRoomsCountError) {
            _uiState.update {
                it.copy(
                    typeError = typeResult.errorMessage,
                    priceError = priceResult.errorMessage,
                    areaError = areaResult.errorMessage,
                    addressError = addressResult.errorMessage,
                    agentError = agentResult.errorMessage,
                    photosError = photoResult.errorMessage,
                    dateError = datesResult.errorMessage,
                    statusError = statusResult.errorMessage,
                    roomsCountError = roomsCountResult?.errorMessage,
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
                    photosError = null,
                    dateError = null,
                    statusError = null,
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
        }
        return finalUri
    }
}