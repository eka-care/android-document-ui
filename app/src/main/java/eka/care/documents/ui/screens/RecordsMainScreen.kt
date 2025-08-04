package eka.care.documents.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import eka.care.documents.ui.components.bottomSheet.RecordsBottomSheetContent
import eka.care.documents.ui.model.TabItem
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.utility.Mode
import eka.care.documents.ui.utility.RecordsAction
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.RecordModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsMainScreen(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    navigateToCreateCase: () -> Unit
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
            RecordsBottomSheetContent(
                onClick = {
                    when (it) {
                        RecordsAction.ACTION_OPEN_SHEET -> {
                            openSheet()
                        }

                        RecordsAction.ACTION_CLOSE_SHEET -> {
                            closeSheet()
                        }

                        RecordsAction.ACTION_OPEN_DELETE_DIALOG -> {
//                            showDeleteDialog = true
                        }
                    }
                },
                onShare = {
//                    scope.launch {
//                        viewModel.getRecordDetails()
//                        viewModel.cardClickData.value?.let { record ->
//                            val filePaths = viewModel.cardClickData.value?.files ?: emptyList()
//                            if (filePaths.isEmpty()) {
//                                Toast.makeText(
//                                    context,
//                                    "Syncing data, please wait!",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            } else {
//                                FileSharing().shareFiles(
//                                    context,
//                                    filePaths.mapNotNull { file -> file.filePath })
//                            }
//                        }
//                    }
                },
                onEditDocument = {
//                    eka.care.records.ui.presentation.screens.syncRecords(
//                        filterIds = filterIdsToProcess,
//                        ownerId = navData.ownerId,
//                        context = context
//                    )
                },
                pickPdf = {
//                    MediaPickerManager.pickPdf()
                },
                scanDocument = {
//                    MediaPickerManager.scanDocument(context)
                },
                cameraLauncher = {
//                    MediaPickerManager.takePhoto(context = context, provider = "eka.care.doctor.fileprovider.new", onPermissionDenied = {})
                },
                pickImagesFromGallery = {
//                    MediaPickerManager.pickVisualMedia()
                },
                viewModel = viewModel,
                params = params,
                allFilterIds = filterIdsToProcess
            )
        },
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetBackgroundColor = EkaTheme.colors.surfaceContainerLow,
        content = {
            ScreenContent(
                viewModel = viewModel,
                params = params,
                filterIdsToProcess = filterIdsToProcess,
                navigateToCreateCase = navigateToCreateCase,
                openSheet = {
                    openSheet.invoke()
                }
            )
        }
    )
}

@Composable
private fun ScreenContent(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    filterIdsToProcess: List<String>,
    navigateToCreateCase: () -> Unit,
    openSheet: () -> Unit
) {
    val selectedItems = remember { mutableStateListOf<RecordModel>() }
    var selectedTabId by remember { mutableStateOf(TabConstants.ALL_FILES) }
    val context = LocalContext.current

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
                actionText = "Add Record",
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Icon",
                        tint = EkaTheme.colors.onPrimary
                    )
                },
                onClick = {
//                    viewModel.documentBottomSheetType = DocumentBottomSheetType.DocumentUpload
//                    openSheet.invoke()
                    navigateToCreateCase.invoke()
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
                        when(tabId) {
                            TabConstants.ALL_FILES -> {}
                            TabConstants.MEDICAL_CASES -> {}
                        }
                    }
                )

                RecordSortSection(viewModel = viewModel, openSheet = openSheet)

                when(selectedTabId) {
                    TabConstants.ALL_FILES -> {
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
                            openSheet = { openSheet.invoke() },
                            onRefresh = {
                                syncRecords(
                                    filterIds = filterIdsToProcess,
                                    ownerId = params.ownerId,
                                    context = context
                                )
                            }
                        )
                    }

                    TabConstants.MEDICAL_CASES -> {
                        CaseView(
                            cases = emptyList(),
                            onCaseItemClick = { caseItem ->

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
