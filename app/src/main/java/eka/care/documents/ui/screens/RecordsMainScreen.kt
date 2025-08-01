package eka.care.documents.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.components.RecordSortSection
import eka.care.documents.ui.components.RecordTabs
import eka.care.documents.ui.components.RecordsScreenContent
import eka.care.documents.ui.components.RecordsSearchBar
import eka.care.documents.ui.model.TabItem
import eka.care.documents.ui.utility.Mode
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.RecordModel

@Composable
fun RecordsMainScreen(
    viewModel: RecordsViewModel
) {
    val selectedItems = remember { mutableStateListOf<RecordModel>() }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(EkaTheme.colors.surface),
        topBar = {
            RecordsSearchBar()
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues),
                contentPadding = PaddingValues(0.dp)
            ) {
                stickyHeader {
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
                }
                item {
                    RecordSortSection(
                        sortBy = "Upload date",
                        onSortByChange = {},
                        onViewModeToggle = {},
                        isGridView = true
                    )
                }
                item {
                    RecordsScreenContent(
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
//                            syncRecords(
//                                filterIds = filterIdsToProcess,
//                                ownerId = navData.ownerId,
//                                context = context
//                            )
                        }
                    )
                }
            }
        }
    )
}