package eka.care.documents.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.eka.ui.buttons.EkaButton
import com.eka.ui.buttons.EkaButtonShape
import com.eka.ui.buttons.EkaButtonSize
import com.eka.ui.buttons.EkaButtonStyle
import com.eka.ui.buttons.EkaIcon
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.components.RecordsScreenContent
import eka.care.documents.ui.components.bottomSheet.CaseDetailsBottomSheet
import eka.care.documents.ui.components.bottomSheet.CaseOptionsBottomSheet
import eka.care.documents.ui.components.bottomSheet.UpdateCaseBottomSheet
import eka.care.documents.ui.components.syncRecords
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.utility.DocumentBottomSheetType
import eka.care.documents.ui.utility.RecordsAction
import eka.care.documents.ui.viewmodel.CaseDetailsOptions
import eka.care.documents.ui.viewmodel.CaseDetailsViewModel
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.RecordModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseDetailsScreen(
    viewModel: RecordsViewModel,
    caseDetailsViewModel: CaseDetailsViewModel,
    params: MedicalRecordsNavModel,
    caseId: String,
    openSmartReport: (data: RecordModel) -> Unit,
    openRecordViewer: (data: RecordModel) -> Unit,
    onBackPressed: () -> Unit = {}
) {
    val context = LocalContext.current
    val owners = mutableListOf<String>().apply {
        if (params.ownerId.isNotEmpty()) {
            add(params.ownerId)
        }
        if (!params.links.isNullOrBlank()) {
            params.links.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() && it != params.ownerId }
                .forEach { add(it) }
        }
    }
    val sheetType = caseDetailsViewModel.caseBottomSheet
    var showDeleteDialog by remember { mutableStateOf(false) }
    LaunchedEffect(params) {
        syncRecords(
            businessId = params.businessId,
            owners = owners,
            context = context,
        )
        viewModel.fetchRecordsCount(
            businessId = params.businessId,
            owners = owners,
        )
        viewModel.fetchRecords(
            businessId = params.businessId,
            owners = owners,
            caseId = caseId
        )
    }
    val sheetState = rememberModalBottomSheetState(
        initialValue = Hidden,
        skipHalfExpanded = true
    )
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

    if (showDeleteDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = "Confirm Delete") },
            text = { Text(text = "Are you sure you want to delete this case?") },
            confirmButton = {
                EkaButton(
                    label = "Yes",
                    shape = EkaButtonShape.ROUNDED,
                    size = EkaButtonSize.SMALL,
                    style = EkaButtonStyle.TEXT,
                    onClick = {
                        caseDetailsViewModel.deleteCase(businessId = params.businessId, caseId = caseId)
                        showDeleteDialog = false
                        onBackPressed()
                    },
                )
            },
            dismissButton = {
                EkaButton(
                    shape = EkaButtonShape.ROUNDED,
                    style = EkaButtonStyle.TEXT,
                    size = EkaButtonSize.SMALL,
                    label = "No",
                    onClick = {
                        showDeleteDialog = false
                    }
                )
            }
        )
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            SheetContent(
                businessId = params.businessId,
                owners = owners,
                caseDetailsViewModel = caseDetailsViewModel,
                viewModel = viewModel,
                sheetType = sheetType,
                caseId = caseId,
                openSheet = {
                    openSheet.invoke()
                },
                closeSheet = {
                    closeSheet.invoke()
                },
                onDeleteCase = {
                    showDeleteDialog = true
                }
            )
        },
        content = {
            Content(
                viewModel = viewModel,
                params = params,
                caseId = caseId,
                openSmartReport = openSmartReport,
                openRecordViewer = openRecordViewer,
                onBackPressed = onBackPressed,
                onMoreOptionSelection = {
                    caseDetailsViewModel.caseBottomSheet = CaseDetailsOptions.MORE
                    openSheet.invoke()
                }
            )
        },
        sheetBackgroundColor = Color.White,
        sheetShape = EkaTheme.shapes.medium
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    caseId: String,
    openSmartReport: (data: RecordModel) -> Unit,
    openRecordViewer: (data: RecordModel) -> Unit,
    onBackPressed: () -> Unit = {},
    onMoreOptionSelection: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .background(EkaTheme.colors.surface)
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EkaTheme.colors.surface
                ),
                title = {
                    Text(
                        text = "Case Details",
                        style = EkaTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .padding(2.dp),
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onMoreOptionSelection,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .padding(2.dp),
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "More",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            RecordsScreenContent(
                modifier = Modifier.padding(paddingValues),
                viewModel = viewModel,
                params = params,
                caseId = caseId,
                openSmartReport = openSmartReport,
                openRecordViewer = openRecordViewer
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth().background(Color.White)
            ) {
                EkaButton(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    label = "Add Record to this Case",
                    shape = EkaButtonShape.SQUARE,
                    size = EkaButtonSize.MEDIUM,
                    style = EkaButtonStyle.FILLED,
                    leadingIcon = EkaIcon(Icons.Default.Add),
                    onClick = {
                        viewModel.documentBottomSheetType = DocumentBottomSheetType.DocumentUpload
                    }
                )
            }
        }
    )
}

@Composable
private fun SheetContent(
    viewModel: RecordsViewModel,
    caseDetailsViewModel: CaseDetailsViewModel,
    sheetType: CaseDetailsOptions,
    businessId: String,
    owners: List<String>,
    caseId: String,
    openSheet: () -> Unit,
    closeSheet: () -> Unit,
    onDeleteCase: () -> Unit
) {
    val context = LocalContext.current
    when(sheetType) {
        CaseDetailsOptions.MORE -> {
            CaseOptionsBottomSheet(
                onClick = {
                    when (it) {
                        RecordsAction.ACTION_CASE_DETAILS -> {
                            caseDetailsViewModel.caseBottomSheet = CaseDetailsOptions.CASE_DETAILS
                            openSheet()
                        }
                        RecordsAction.ACTION_EDIT_CASE -> {
                            caseDetailsViewModel.caseBottomSheet = CaseDetailsOptions.EDIT_CASE
                            openSheet()
                        }
                        RecordsAction.ACTION_DELETE_CASE -> {
                            onDeleteCase()
                        }
                    }
                }
            )
        }
        CaseDetailsOptions.CASE_DETAILS -> {
            CaseDetailsBottomSheet(
                viewModel = viewModel,
                caseId = caseId
            )
        }
        CaseDetailsOptions.EDIT_CASE -> {
            UpdateCaseBottomSheet(
                businessId = businessId,
                caseId = caseId,
                viewModel = viewModel,
                closeSheet = {
                    closeSheet.invoke()
                }
            )
        }
    }
}