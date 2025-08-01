package eka.care.documents.ui.state

import eka.care.records.client.model.RecordModel

sealed class DocumentPreviewState {
    data object Loading : DocumentPreviewState()
    data class Error(val message: String) : DocumentPreviewState()
    data class Success(val data: RecordModel) : DocumentPreviewState()
}