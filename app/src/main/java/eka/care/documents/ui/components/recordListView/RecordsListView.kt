package eka.care.documents.ui.components.recordListView

import androidx.compose.runtime.Composable
import eka.care.documents.ui.state.RecordsState
import eka.care.records.client.model.RecordModel
import eka.care.records.ui.presentation.screens.RecordEmptyState

@Composable
fun RecordsListView(
    state: RecordsState,
    onUploadRecordClick: () -> Unit,
    onRecordClick: (record: RecordModel) -> Unit,
    onMoreOptionsClick: (record: RecordModel) -> Unit,
) {
    when (state) {
        is RecordsState.Loading -> RecordsListShimmer()
        is RecordsState.EmptyState -> RecordEmptyState(onClick = onUploadRecordClick)
        is RecordsState.Error -> {}
        is RecordsState.Success -> {
            val records = (state as? RecordsState.Success)?.data ?: emptyList()
            RecordsList(
                records = records,
                onClick = onRecordClick,
                onMoreOptionsClick = onMoreOptionsClick
            )
        }
    }
}