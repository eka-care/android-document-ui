package eka.care.documents.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.components.RecordSortSection
import eka.care.documents.ui.components.RecordTabs
import eka.care.documents.ui.components.RecordsScreenContent
import eka.care.documents.ui.components.RecordsSearchBar
import eka.care.documents.ui.components.bottomSheet.RecordSortBottomSheet
import eka.care.documents.ui.model.TabItem
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.utility.Mode
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.RecordModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsMainScreen(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel
) {
    val sheetState = rememberModalBottomSheetState(Hidden)
    val scope = rememberCoroutineScope()

    val openSheet = {
        scope.launch {
            sheetState.show()
        }
    }
    val closeSheet = {
        scope.launch {
            sheetState.hide()
        }
    }

    val filterIdsToProcess = mutableListOf<String>().apply {
        if (params.filterId?.isNotEmpty() == true) {
            add(params.filterId)
        }
        if (!params.links.isNullOrBlank()) {
            params.links.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() && it != params.filterId }
                .forEach { add(it) }
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            RecordSortBottomSheet(
                selectedSort = viewModel.sortBy.value,
                onCloseClick = {
                    closeSheet.invoke()
                },
                onClick = {
                    viewModel.sortBy.value = it
                    viewModel.fetchRecords(
                        filterIds = filterIdsToProcess,
                        ownerId = params.ownerId
                    )
                    closeSheet.invoke()
                },
            )
        },
        content = {
            ScreenContent(
                viewModel = viewModel,
                params = params,
                filterIdsToProcess = filterIdsToProcess,
                openSortBySheet = { openSheet.invoke() }
            )
        }
    )
}

@Composable
private fun ScreenContent(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    openSortBySheet: () -> Unit,
    filterIdsToProcess: List<String>
) {
    val selectedItems = remember { mutableStateListOf<RecordModel>() }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(EkaTheme.colors.surface),
        topBar = {
            RecordsSearchBar()
        },
        floatingActionButton = {

        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues),
            ) {
                RecordTabs(
                    tabs = listOf(
                        TabItem(id = "all_files", title = "All Files", isSelected = true),
                        TabItem(
                            id = "medical_cases",
                            title = "Medical Cases",
                            isSelected = false
                        )
                    )
                )
                RecordSortSection(
                    sortBy = "Upload date",
                    openSortBySheet = openSortBySheet,
                    onViewModeToggle = {
                        viewModel.toggleDocumentViewType()
                    },
                    isGridView = false
                )
                RecordsScreenContent(
                    params = params,
                    viewModel = viewModel,
                    mode = Mode.VIEW,
                    selectedItems = selectedItems,
                    onSelectedItemsChange = { items ->
//                            selectedItems.clear()
//                            selectedItems.addAll(items)
                    },
                    openSmartReport = {},
                    openRecordViewer = {},
                    onRefresh = {
                        syncRecords(
                            filterIds = filterIdsToProcess,
                            ownerId = params.ownerId,
                            context = context
                        )
                    }
                )
            }
        }
    )
}