package eka.care.documents.ui.components.recordListView

import androidx.compose.runtime.Composable
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.state.RecordsState
import eka.care.records.client.model.RecordModel
import eka.care.records.ui.presentation.screens.RecordEmptyState

@Composable
fun RecordsListView(
    state: RecordsState,
    documentTypes : List<MedicalRecordsNavModel.DocumentType> = emptyList(),
    onUploadRecordClick: () -> Unit,
    onRecordClick: (record: RecordModel) -> Unit,
    onMoreOptionsClick: (record: RecordModel) -> Unit,
) {
    when (state) {
        is RecordsState.Loading -> RecordsListShimmer()
        is RecordsState.EmptyState -> RecordEmptyState(onClick = onUploadRecordClick)
        is RecordsState.Error -> {}
        is RecordsState.Success -> {
            RecordsList(
                records = state.data,
                onClick = onRecordClick,
                onMoreOptionsClick = onMoreOptionsClick,
                documentTypes = documentTypes
            )
        }
    }
}