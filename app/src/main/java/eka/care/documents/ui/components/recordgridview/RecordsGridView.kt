package eka.care.documents.ui.components.recordgridview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.state.RecordsState
import eka.care.documents.ui.utility.Mode
import eka.care.records.client.model.RecordModel
import eka.care.records.ui.presentation.screens.RecordEmptyState

@Composable
fun RecordsGridView(
    state: RecordsState,
    documentTypes : List<MedicalRecordsNavModel.DocumentType> = emptyList(),
    mode: Mode,
    selectedItems: SnapshotStateList<RecordModel> ? = null,
    onSelectedItemsChange: (List<RecordModel>) -> Unit,
    onUploadRecordClick: () -> Unit,
    onRecordClick: (record: RecordModel) -> Unit,
    onRetry: (record: RecordModel) -> Unit,
    onMoreOptionsClick: (record: RecordModel) -> Unit,
) {
    when (state) {
        is RecordsState.Loading -> {
            RecordsGridShimmer()
        }
        is RecordsState.EmptyState -> RecordEmptyState(onClick = onUploadRecordClick)
        is RecordsState.Error -> {}
        is RecordsState.Success -> {
            RecordsGrid(
                records = state.data,
                onClick = onRecordClick,
                onRetry = onRetry,
                onMoreOptionsClick = onMoreOptionsClick,
                mode = mode,
                selectedItems = selectedItems,
                onSelectedItemsChange = onSelectedItemsChange,
                documentTypes = documentTypes
            )
        }
    }
}