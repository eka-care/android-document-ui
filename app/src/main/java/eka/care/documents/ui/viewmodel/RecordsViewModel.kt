package eka.care.documents.ui.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import eka.care.documents.ui.state.CasesState
import eka.care.documents.ui.state.RecordsCountByType
import eka.care.documents.ui.state.RecordsState
import eka.care.documents.ui.state.TagsState
import eka.care.documents.ui.state.UpsertRecordState
import eka.care.documents.ui.utility.DocumentBottomSheetType
import eka.care.documents.ui.utility.DocumentViewType
import eka.care.records.client.model.RecordModel
import eka.care.records.client.model.SortOrder
import eka.care.records.client.utils.Records
import eka.care.records.sync.recordSyncFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import java.io.File

class RecordsViewModel(val app: Application) : AndroidViewModel(app) {
    private val recordsManager = Records.getInstance(context = app.applicationContext)

    private var job: Job? = null
    private var recordsCountJob: Job? = null
    private var caseJob: Job? = null
    private var tagsJob: Job? = null

    val cardClickData = mutableStateOf<RecordModel?>(null)

    private val _getRecordsState = MutableStateFlow<RecordsState>(RecordsState.Loading)
    val getRecordsState: StateFlow<RecordsState> = _getRecordsState

    val searchActive = mutableStateOf(false)

    private val _tagsState = MutableStateFlow<TagsState>(TagsState.Loading)
    val tagsState: StateFlow<TagsState> = _tagsState

    private val _getCasesState = MutableStateFlow<CasesState>(CasesState.Loading)
    val getCasesState: StateFlow<CasesState> = _getCasesState

    private val _getCaseDetailsState = MutableStateFlow<CasesState>(CasesState.Loading)
    val getCaseDetailsState: StateFlow<CasesState> = _getCaseDetailsState

    private val _upsertRecordsState = MutableStateFlow<UpsertRecordState>(UpsertRecordState.NONE)
    val upsertRecordsState: StateFlow<UpsertRecordState> = _upsertRecordsState

    private val _syncing = MutableStateFlow<Boolean>(false)
    val syncing: StateFlow<Boolean> = _syncing

    private val _getAvailableDocTypes = MutableStateFlow(RecordsCountByType())
    val recordsCountByType: StateFlow<RecordsCountByType> = _getAvailableDocTypes

    val sortBy = mutableStateOf(SortOrder.CREATED_AT_DSC)
    var documentType = mutableStateOf<String?>(null)
    var tags = mutableStateOf<List<String>>(emptyList())

    val updateDocumentType = { type: String? ->
        documentType.value = type
    }

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
        businessId: String,
        owners: List<String>
    ) {
        recordsCountJob?.cancel()
        _getAvailableDocTypes.value = RecordsCountByType(isLoading = true)
        recordsCountJob = viewModelScope.launch {
            val documentFlow = recordsManager.getRecordsCountGroupByType(
                businessId = businessId,
                ownerIds = owners
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

    fun enableRecordSearch() {
        searchActive.value = true
    }

    fun disableRecordSearch() {
        searchActive.value = false
    }

    fun fetchRecords(owners: List<String>, businessId: String, caseId: String? = null) {
        job?.cancel()
        job = viewModelScope.launch {
            val documentFlow = recordsManager.getRecords(
                businessId = businessId,
                ownerIds = owners,
                caseId = caseId,
                sortOrder = sortBy.value,
                documentType = documentType.value,
                tags = tags.value
            )
            documentFlow
                .cancellable()
                .collect { records ->
                    _getRecordsState.value = if (records.isEmpty()) {
                        RecordsState.EmptyState
                    } else {
                        RecordsState.Success(data = records)
                    }
                }
        }
    }

    fun searchRecords(
        businessId: String,
        owners: List<String>,
        query: String,
    ) {
        if (query.isEmpty()) {
            RecordsState.EmptyState
            return
        }
        job?.cancel()
        job = viewModelScope.launch {
            val records = recordsManager.searchRecords(
                businessId = businessId,
                ownerIds = owners,
                query = query
            )
            _getRecordsState.value = if (records.isEmpty()) {
                RecordsState.EmptyState
            } else {
                RecordsState.Success(data = records)
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
        businessId: String,
        ownerId: String,
        caseId: String?,
        documentType: String,
        documentDate: Long?,
        isAbhaLinked : Boolean = true,
    ) {
        _upsertRecordsState.value = UpsertRecordState.Loading
        viewModelScope.launch {
            val recordId = recordsManager.addNewRecord(
                files = files,
                businessId = businessId,
                ownerId = ownerId,
                caseId = caseId,
                documentType = documentType,
                documentDate = documentDate,
                isAbhaLinked = isAbhaLinked
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

    fun getRecordsSyncState(businessId: String) {
        viewModelScope.launch {
            WorkManager.getInstance(app.applicationContext).recordSyncFlow(businessId).collect {
                it?.let { info ->
                    _syncing.value = it.progress.getBoolean("syncing", false)
                }
            }
        }
    }

    fun deleteRecord(localId: String) {
        viewModelScope.launch {
            recordsManager.deleteRecords(ids = listOf(localId))
        }
    }

    fun createCase(
        businessId: String,
        ownerId: String,
        name: String,
        type: String
    ) {
        viewModelScope.launch {
            recordsManager.createCase(
                businessId = businessId,
                ownerId = ownerId,
                name = name,
                type = type,
            )
            recordsManager.syncRecords(ownerId)
        }
    }

    fun getCases(businessId: String, ownerId: String) {
        caseJob?.cancel()
        caseJob = viewModelScope.launch {
            recordsManager.readCases(
                businessId = businessId,
                ownerId = ownerId
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

    fun updateCase(businessId: String, caseId: String, name: String, type: String) {
        viewModelScope.launch {
            recordsManager.updateEncounter(
                caseId = caseId,
                name = name,
                type = type
            )
            recordsManager.syncRecords(businessId)
        }
    }

    fun getCaseDetails(caseId: String) {
        viewModelScope.launch {
            val caseDetails = recordsManager.getCaseWithRecords(caseId = caseId)
            caseDetails?.let {
                _getCaseDetailsState.value = CasesState.Success(data = listOf(it))
            }
        }
    }

    fun assignRecordToCase(
        recordId: String,
        caseId: String
    ) {
        viewModelScope.launch {
            recordsManager.assignRecordToCase(
                recordId = recordId,
                caseId = caseId
            )
        }
    }

    fun getTags(businessId: String, owners: List<String>) {
        tagsJob?.cancel()
        tagsJob = viewModelScope.launch {
            recordsManager.getTags(
                businessId = businessId,
                ownerIds = owners
            ).cancellable()
                .collect { tags ->
                    _tagsState.value = TagsState.Success(data = tags)
                    Log.d("Tags", tags.toString())
                }
        }
    }
}