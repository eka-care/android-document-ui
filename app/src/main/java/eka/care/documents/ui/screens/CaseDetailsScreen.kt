package eka.care.documents.ui.screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import eka.care.documents.ui.components.RecordsScreenContent
import eka.care.documents.ui.utility.Mode
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.RecordModel

@Composable
fun CaseDetailsScreen(
    viewModel: RecordsViewModel,
    caseId: String,
    ownerId: String,
    filterId: String,
    onBackPressed: () -> Unit = {},
) {
    val selectedItems = remember { mutableStateListOf<RecordModel>() }
    Scaffold(
        topBar = {

        },
        content = { paddingValues ->
            RecordsScreenContent(
                viewModel = viewModel,
                mode = Mode.VIEW,
                ownerId = ownerId,
                filterIdsToProcess = listOf(filterId),
                selectedItems = selectedItems,
                onSelectedItemsChange = { items ->
//                            selectedItems.clear()
//                            selectedItems.addAll(items)
                },
                openSmartReport = {

                },
                openRecordViewer = {

                },
                openSheet = {

                },
                onRefresh = {

                }
            )
        }
    )
}