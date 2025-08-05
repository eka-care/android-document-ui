package eka.care.documents.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import eka.care.documents.ui.components.recordListView.RecordsListView
import eka.care.documents.ui.components.recordgridview.RecordsGridView
import eka.care.documents.ui.utility.DocumentBottomSheetType
import eka.care.documents.ui.utility.DocumentViewType
import eka.care.documents.ui.utility.Mode
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.RecordModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RecordsScreenContent(
    viewModel: RecordsViewModel,
    mode: Mode,
    selectedItems: SnapshotStateList<RecordModel>,
    onSelectedItemsChange: (List<RecordModel>) -> Unit,
    openSmartReport: (data: RecordModel) -> Unit,
    openRecordViewer: (data: RecordModel) -> Unit,
    openSheet: () -> Unit,
    onRefresh: () -> Unit,
) {
    val context = LocalContext.current
    val isRefreshing by viewModel.isRefreshing
    val recordsState by viewModel.getRecordsState.collectAsState()

    val handleRecordClick: (record: RecordModel) -> Unit = { record ->
        if (record.isSmart) {
            openSmartReport(record)
        } else {
            openRecordViewer(record)
        }
    }

    val handleRecordUploadClick: () -> Unit = {
        viewModel.documentBottomSheetType = DocumentBottomSheetType.DocumentUpload
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        when (viewModel.documentViewType) {
            DocumentViewType.GridView -> RecordsGridView(
                state = recordsState,
                mode = mode,
                selectedItems = selectedItems,
                onSelectedItemsChange = onSelectedItemsChange,
                onRecordClick = handleRecordClick,
                onRetry = {
                    onRefresh.invoke()
                },
                onUploadRecordClick = handleRecordUploadClick,
                onMoreOptionsClick = { record ->
                    viewModel.documentBottomSheetType = DocumentBottomSheetType.DocumentOptions
                    viewModel.cardClickData.value = record
                    openSheet()
                }
            )

            DocumentViewType.ListView -> RecordsListView(
                state = recordsState,
                onRecordClick = handleRecordClick,
                onUploadRecordClick = handleRecordUploadClick,
                onMoreOptionsClick = { record ->
                    viewModel.documentBottomSheetType = DocumentBottomSheetType.DocumentOptions
                    viewModel.cardClickData.value = record
                    openSheet()
                }
            )
        }
    }
}