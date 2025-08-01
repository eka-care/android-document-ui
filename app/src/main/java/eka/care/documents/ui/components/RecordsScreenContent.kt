package eka.care.documents.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eka.care.documents.ui.components.recordListView.RecordsListView
import eka.care.documents.ui.components.recordgridview.RecordsGridView
import eka.care.documents.ui.utility.DocumentBottomSheetType
import eka.care.documents.ui.utility.DocumentViewType
import eka.care.documents.ui.utility.Mode
import eka.care.records.client.model.RecordModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RecordsScreenContent(
    paddingValues: PaddingValues,
    mode: Mode,
    selectedItems: SnapshotStateList<RecordModel>,
    onSelectedItemsChange: (List<RecordModel>) -> Unit,
    openSmartReport: (data: RecordModel) -> Unit,
    openRecordViewer: (data: RecordModel) -> Unit,
    onRefresh: () -> Unit,
) {
    val viewModel = koinViewModel<RecordsViewModel>()

    val isRefreshing by viewModel.isRefreshing
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing, onRefresh = onRefresh
    )
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .pullRefresh(pullRefreshState)
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
                }
            )

            DocumentViewType.ListView -> RecordsListView(
                state = recordsState,
                onRecordClick = handleRecordClick,
                onUploadRecordClick = handleRecordUploadClick,
                onMoreOptionsClick = { record ->
                    viewModel.documentBottomSheetType = DocumentBottomSheetType.DocumentOptions
                    viewModel.cardClickData.value = record
                }
            )
        }
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = isRefreshing,
            state = pullRefreshState,
            scale = true
        )
    }
}