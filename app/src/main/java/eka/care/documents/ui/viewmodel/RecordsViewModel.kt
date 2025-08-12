package eka.care.documents.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import eka.care.documents.ui.state.CasesState
import eka.care.documents.ui.state.RecordsCountByType
import eka.care.documents.ui.state.RecordsState
import eka.care.documents.ui.state.UpsertRecordState
import eka.care.documents.ui.utility.DocumentBottomSheetType
import eka.care.documents.ui.utility.DocumentViewType
import eka.care.records.client.model.RecordModel
import eka.care.records.client.model.SortOrder
import eka.care.records.client.utils.Records
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import java.io.File

class RecordsViewModel(val app: Application) : AndroidViewModel(app) {
    private val recordsManager = Records.getInstance(
        context = app.applicationContext,
        token = ""
    )

    private var job: Job? = null
    private var recordsCountJob: Job? = null
    private var caseJob: Job? = null
    val cardClickData = mutableStateOf<RecordModel?>(null)

    private val _getRecordsState = MutableStateFlow<RecordsState>(RecordsState.Loading)
    val getRecordsState: StateFlow<RecordsState> = _getRecordsState

    private val _getCasesState = MutableStateFlow<CasesState>(CasesState.Loading)
    val getCasesState: StateFlow<CasesState> = _getCasesState

    private val _upsertRecordsState = MutableStateFlow<UpsertRecordState>(UpsertRecordState.NONE)
    val upsertRecordsState: StateFlow<UpsertRecordState> = _upsertRecordsState

    private val _getAvailableDocTypes = MutableStateFlow(RecordsCountByType())
    val recordsCountByType: StateFlow<RecordsCountByType> = _getAvailableDocTypes

    val sortBy = mutableStateOf(SortOrder.CREATED_AT_DSC)
    var documentType = mutableStateOf<String?>(null)
    val updateDocumentType = { type: String? ->
        documentType.value = type
    }

    var isRefreshing = mutableStateOf(false)

    var documentBottomSheetType by mutableStateOf<DocumentBottomSheetType?>(null)

    var documentViewType by mutableStateOf(DocumentViewType.GridView)

    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri: StateFlow<Uri?> = _photoUri

    fun updatePhotoUri(uri: Uri?) {
        _photoUri.value = uri
    }

    fun updateRecordState(state: UpsertRecordState) {
        _upsertRecordsState.value = state
    }

    fun toggleDocumentViewType() {
        documentViewType = if (documentViewType == DocumentViewType.GridView) {
            DocumentViewType.ListView
        } else {
            DocumentViewType.GridView
        }
    }

    fun fetchRecordsCount(
        filterIds: List<String>,
        ownerId: String,
    ) {
        recordsCountJob?.cancel()
        _getAvailableDocTypes.value = RecordsCountByType(isLoading = true)
        recordsCountJob = viewModelScope.launch {
            val documentFlow = recordsManager.getRecordsCountGroupByType(
                filterIds = filterIds,
                ownerId = ownerId
            )
            documentFlow
                .cancellable()
                .collect { records ->
                    _getAvailableDocTypes.value = RecordsCountByType(
                        data = records,
                        isLoading = false
                    )
                }

        }
    }

    fun fetchRecords(filterIds: List<String>, ownerId: String, caseId: String? = null) {
        job?.cancel()
        _getRecordsState.value = RecordsState.Loading
        job = viewModelScope.launch {
            isRefreshing.value = true
            val documentFlow = recordsManager.getRecords(
                filterIds = filterIds,
                ownerId = ownerId,
                caseId = caseId,
                sortOrder = sortBy.value,
                documentType = documentType.value
            )
            documentFlow
                .cancellable()
                .collect { records ->
                    _getRecordsState.value = if (records.isEmpty()) {
                        RecordsState.EmptyState
                    } else {
                        isRefreshing.value = false
                        RecordsState.Success(data = records)
                    }
                }
        }
    }

    suspend fun getRecordDetails() {
        cardClickData.value?.let { record ->
            val recordDetails = recordsManager.getRecordDetailsById(record.id)
            cardClickData.value = recordDetails
        }
    }

    fun createRecord(
        files: List<File>,
        ownerId: String,
        filterId: String?,
        caseId: String?,
        documentType: String,
        documentDate: Long?,
    ) {
        _upsertRecordsState.value = UpsertRecordState.Loading
        viewModelScope.launch {
            val recordId = recordsManager.addNewRecord(
                files = files,
                ownerId = ownerId,
                filterId = filterId,
                caseId = caseId,
                documentType = documentType,
                documentDate = documentDate,
            )
            _upsertRecordsState.value = if (recordId != null) {
                UpsertRecordState.Success(recordId)
            } else {
                UpsertRecordState.Error("Failed to create record")
            }
        }
    }

    fun updateRecord(localId: String, docType: String, docDate: Long) {
        _upsertRecordsState.value = UpsertRecordState.Loading
        viewModelScope.launch {
            val recordId = recordsManager.updateRecord(
                id = localId,
                documentType = docType,
                documentDate = docDate,
            )
            _upsertRecordsState.value = if (recordId != null) {
                UpsertRecordState.Success(recordId)
            } else {
                UpsertRecordState.Error("Failed to update record")
            }
        }
    }

    fun deleteRecord(localId: String) {
        viewModelScope.launch {
            recordsManager.deleteRecords(ids = listOf(localId))
        }
    }

    fun createCase(
        ownerId: String,
        filterId: String?,
        name: String,
        type: String
    ) {
        viewModelScope.launch {
            recordsManager.createCase(
                name = name,
                type = type,
                ownerId = ownerId,
                filterId = filterId,
            )
        }
    }

    fun getCases(
        ownerId: String,
        filterId: String?
    ) {
        caseJob?.cancel()
        caseJob = viewModelScope.launch {
            recordsManager.readCases(
                ownerId = ownerId,
                filterId = filterId
            ).cancellable()
                .collect { cases ->
                    _getCasesState.value = if (cases.isEmpty()) {
                        CasesState.EmptyState
                    } else {
                        CasesState.Success(data = cases)
                    }
                }
        }
    }
}