package eka.care.documents.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.components.RecordSortSection
import eka.care.documents.ui.components.RecordTabs
import eka.care.documents.ui.components.RecordsScreenContent
import eka.care.documents.ui.components.RecordsSearchBar
import eka.care.documents.ui.model.TabItem
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.utility.Mode
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.RecordModel

@Composable
fun RecordsMainScreen(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel
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
                    onSortByChange = {},
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