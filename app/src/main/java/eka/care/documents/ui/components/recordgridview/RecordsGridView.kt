package eka.care.documents.ui.components.recordgridview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import eka.care.records.client.model.RecordModel
import eka.care.records.ui.presentation.screens.Mode
import eka.care.records.ui.presentation.screens.RecordEmptyState
import eka.care.documents.ui.state.RecordsState

@Composable
fun RecordsGridView(
    state: RecordsState,
    mode: Mode,
    selectedItems: SnapshotStateList<RecordModel>,
    onSelectedItemsChange: (List<RecordModel>) -> Unit,
    onUploadRecordClick: () -> Unit,
    onRecordClick: (record: RecordModel) -> Unit,
    onRetry: (record: RecordModel) -> Unit,
    onMoreOptionsClick: (record: RecordModel) -> Unit,
) {
    when (state) {
        is RecordsState.Loading -> {}
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
            )
        }
    }
}