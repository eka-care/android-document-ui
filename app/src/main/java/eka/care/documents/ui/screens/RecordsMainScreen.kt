package eka.care.documents.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.eka.ui.fab.EkaFloatingActionButton
import com.eka.ui.fab.FabColor
import com.eka.ui.fab.FabType
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.components.RecordSortSection
import eka.care.documents.ui.components.RecordTabs
import eka.care.documents.ui.components.RecordsScreenContent
import eka.care.documents.ui.components.RecordsSearchBar
import eka.care.documents.ui.components.recordcaseview.CaseView
import eka.care.documents.ui.model.TabItem
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.utility.DocumentBottomSheetType
import eka.care.documents.ui.utility.Mode
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.CaseModel
import eka.care.records.client.model.RecordModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsMainScreen(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    navigateToCreateCase: () -> Unit,
    openSmartReport: (data: RecordModel) -> Unit,
    openRecordViewer: (data: RecordModel) -> Unit,
    openCaseDetails: (caseModel: CaseModel) -> Unit
) {
    ScreenContent(
        viewModel = viewModel,
        params = params,
        navigateToCreateCase = navigateToCreateCase,
        openSmartReport = openSmartReport,
        openRecordViewer = openRecordViewer,
        openCaseDetails = openCaseDetails
    )
}

@Composable
private fun ScreenContent(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    navigateToCreateCase: () -> Unit,
    openSmartReport: (data: RecordModel) -> Unit = {},
    openRecordViewer: (data: RecordModel) -> Unit = {},
    openCaseDetails: (caseModel: CaseModel) -> Unit
) {
    val selectedItems = remember { mutableStateListOf<RecordModel>() }
    var selectedTabId by remember { mutableStateOf(TabConstants.ALL_FILES) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(EkaTheme.colors.surface),
        topBar = {
            RecordsSearchBar()
        },
        floatingActionButton = {
            EkaFloatingActionButton(
                fabType = FabType.NORMAL,
                fabColor = FabColor.PRIMARY_CONTAINER,
                actionText = if (selectedTabId == TabConstants.ALL_FILES) "Add Record" else "Add Case",
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Icon",
                        tint = EkaTheme.colors.onPrimary
                    )
                },
                onClick = {
                    if (selectedTabId == TabConstants.ALL_FILES) {
                        viewModel.documentBottomSheetType = DocumentBottomSheetType.DocumentUpload
                    } else {
                        navigateToCreateCase.invoke()
                    }
                }
            )
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
                        TabItem(
                            id = TabConstants.ALL_FILES,
                            title = "All Files",
                            isSelected = selectedTabId == TabConstants.ALL_FILES
                        ),
                        TabItem(
                            id = TabConstants.MEDICAL_CASES,
                            title = "Medical Cases",
                            isSelected = selectedTabId == TabConstants.MEDICAL_CASES
                        )
                    ),
                    onTabClick = { tabId ->
                        selectedTabId = tabId
                        when (tabId) {
                            TabConstants.ALL_FILES -> {}
                            TabConstants.MEDICAL_CASES -> {}
                        }
                    }
                )

                when (selectedTabId) {
                    TabConstants.ALL_FILES -> {
                        RecordSortSection(
                            viewModel = viewModel,
                            openSheet = {
                                viewModel.documentBottomSheetType = DocumentBottomSheetType.DocumentSort
                            }
                        )
                        RecordsScreenContent(
                            viewModel = viewModel,
                            params = params,
                            mode = Mode.VIEW,
                            selectedItems = selectedItems,
                            onSelectedItemsChange = { items ->
//                            selectedItems.clear()
//                            selectedItems.addAll(items)
                            },
                            openSmartReport = openSmartReport,
                            openRecordViewer = openRecordViewer,
                        )
                    }

                    TabConstants.MEDICAL_CASES -> {
                        CaseView(
                            viewModel = viewModel,
                            params = params,
                            onCaseItemClick = { caseItem ->
                                openCaseDetails(caseItem)
                            }
                        )
                    }
                }
            }
        }
    )
}

object TabConstants {
    const val ALL_FILES = "all_files"
    const val MEDICAL_CASES = "medical_cases"
}
