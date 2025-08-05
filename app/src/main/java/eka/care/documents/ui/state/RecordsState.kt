package eka.care.documents.ui.state

import androidx.annotation.Keep
import eka.care.records.client.model.CaseModel
import eka.care.records.client.model.DocumentTypeCount
import eka.care.records.client.model.RecordModel

sealed class RecordsState {
    data object Loading : RecordsState()
    data class Error(val error: String?) : RecordsState()
    data class Success(val data: List<RecordModel>) : RecordsState()
    data object EmptyState : RecordsState()
}

@Keep
data class RecordsCountByType(
    val isLoading: Boolean = false,
    val data: List<DocumentTypeCount> = emptyList(),
    val error: String? = null,
)

sealed class UpsertRecordState {
    data object NONE: UpsertRecordState()
    data object Loading : UpsertRecordState()
    data class Error(val error: String?) : UpsertRecordState()
    data class Success(val recordId: String) : UpsertRecordState()
}

sealed class CasesState {
    data object Loading : CasesState()
    data class Error(val error: String?) : CasesState()
    data class Success(val data: List<CaseModel>) : CasesState()
    data object EmptyState : CasesState()
}