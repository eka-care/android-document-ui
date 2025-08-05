package eka.care.documents.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.eka.ui.fab.EkaFloatingActionButton
import com.eka.ui.fab.FabColor
import com.eka.ui.fab.FabType
import com.eka.ui.theme.EkaTheme
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import eka.care.documents.ui.activity.AddRecordParams
import eka.care.documents.ui.activity.AddRecordPreviewActivity
import eka.care.documents.ui.components.RecordSortSection
import eka.care.documents.ui.components.RecordTabs
import eka.care.documents.ui.components.RecordsScreenContent
import eka.care.documents.ui.components.RecordsSearchBar
import eka.care.documents.ui.components.bottomSheet.RecordsBottomSheetContent
import eka.care.documents.ui.components.recordcaseview.CaseView
import eka.care.documents.ui.model.TabItem
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.utility.DocumentBottomSheetType
import eka.care.documents.ui.utility.Mode
import eka.care.documents.ui.utility.RecordsAction
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.RecordModel
import eka.care.records.client.utils.MediaPickerManager
import eka.care.records.client.utils.PhotoPickerHost
import eka.care.records.client.utils.Records
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsMainScreen(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    navigateToCreateCase: () -> Unit,
    openSmartReport: (data: RecordModel) -> Unit,
    openRecordViewer: (data: RecordModel) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(Hidden)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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

    val addRecordResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            syncRecords(
                filterIds = filterIdsToProcess,
                ownerId = params.ownerId,
                context = context
            )
        }
    }

    LaunchedEffect(Unit) {
        syncRecords(
            filterIds = filterIdsToProcess,
            ownerId = params.ownerId,
            context = context,
        )
        viewModel.fetchRecordsCount(
            filterIds = filterIdsToProcess,
            ownerId = params.ownerId,
        )
        viewModel.fetchRecords(
            ownerId = params.ownerId,
            filterIds = filterIdsToProcess,
        )
    }

    val mediaPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(3)) { images ->
            val intent = Intent(context, AddRecordPreviewActivity::class.java)
            val paramsJson = JsonObject().apply {
                images.let { addProperty(AddRecordParams.IMAGE_URIS.key, images.joinToString(",")) }
                addProperty(AddRecordParams.FILTER_ID.key, filterIdsToProcess.first())
                addProperty(AddRecordParams.OWNER_ID.key, params.ownerId)
            }
            intent.putExtra(AddRecordParams.PARAMS_KEY, Gson().toJson(paramsJson))
            addRecordResultLauncher.launch(intent)
        }

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                val intent = Intent(context, AddRecordPreviewActivity::class.java)
                val paramsJson = JsonObject().apply {
                    addProperty(AddRecordParams.PDF_URI.key, it.toString())
                    addProperty(AddRecordParams.FILTER_ID.key, filterIdsToProcess.first())
                    addProperty(AddRecordParams.OWNER_ID.key, params.ownerId)
                }
                intent.putExtra(AddRecordParams.PARAMS_KEY, Gson().toJson(paramsJson))
                addRecordResultLauncher.launch(intent)
            }
        }
    )

    val scannerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data =
                    GmsDocumentScanningResult.fromActivityResultIntent(result.data)
                data?.pages?.let { pages ->
                    val intent = Intent(context, AddRecordPreviewActivity::class.java)
                    val paramsJson = JsonObject().apply {
                        pages.let {
                            addProperty(
                                AddRecordParams.IMAGE_URIS.key,
                                pages.joinToString(",") { it.imageUri.toString() })
                        }
                        addProperty(AddRecordParams.FILTER_ID.key, filterIdsToProcess.first())
                        addProperty(AddRecordParams.OWNER_ID.key, params.ownerId)
                    }
                    intent.putExtra(AddRecordParams.PARAMS_KEY, Gson().toJson(paramsJson))
                    addRecordResultLauncher.launch(intent)
                }
            }
        }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.photoUri.value?.let { uri ->
                val intent = Intent(context, AddRecordPreviewActivity::class.java)
                val paramsJson = JsonObject().apply {
                    addProperty(AddRecordParams.IMAGE_URIS.key, uri.toString())
                    addProperty(AddRecordParams.FILTER_ID.key, filterIdsToProcess.first())
                    addProperty(AddRecordParams.OWNER_ID.key, params.ownerId)
                }
                intent.putExtra(AddRecordParams.PARAMS_KEY, Gson().toJson(paramsJson))
                addRecordResultLauncher.launch(intent)
            }
        }
    }

    LaunchedEffect(Unit) {
        MediaPickerManager.setHost(object : PhotoPickerHost {
            override fun takePhoto(intent: Intent, uri: Uri) {
                viewModel.updatePhotoUri(uri)
                cameraLauncher.launch(intent)
            }

            override fun pickPhoto(request: PickVisualMediaRequest) {
                mediaPickerLauncher.launch(request)
            }

            override fun pickPdf() {
                pdfPickerLauncher.launch(arrayOf("application/pdf"))
            }

            override fun scanDocuments(request: IntentSenderRequest) {
                scannerLauncher.launch(request)
            }
        })
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = "Confirm Delete") },
            text = { Text(text = "Are you sure you want to delete this record?") },
            confirmButton = {
                EkaButton(
                    label = "Yes",
                    shape = EkaButtonShape.ROUNDED,
                    size = EkaButtonSize.SMALL,
                    style = EkaButtonStyle.TEXT,
                    onClick = {
                        viewModel.cardClickData.value?.let { record ->
                            viewModel.deleteRecord(
                                localId = record.id
                            )
                            syncRecords(
                                filterIds = filterIdsToProcess,
                                ownerId = params.ownerId,
                                context = context
                            )
                        }
                        showDeleteDialog = false
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
                            showDeleteDialog = true
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
                    syncRecords(
                        filterIds = filterIdsToProcess,
                        ownerId = params.ownerId,
                        context = context
                    )
                },
                pickPdf = {
                    MediaPickerManager.pickPdf()
                },
                scanDocument = {
                    MediaPickerManager.scanDocument(context)
                },
                cameraLauncher = {
                    MediaPickerManager.takePhoto(context = context, provider = "eka.care.doctor.fileprovider.new", onPermissionDenied = {})
                },
                pickImagesFromGallery = {
                    MediaPickerManager.pickVisualMedia()
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
                },
                openSmartReport = openSmartReport,
                openRecordViewer = openRecordViewer,
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
    openSheet: () -> Unit,
    openSmartReport: (data: RecordModel) -> Unit = {},
    openRecordViewer: (data: RecordModel) -> Unit = {}
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
                        openSheet.invoke()
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

                RecordSortSection(viewModel = viewModel, openSheet = openSheet)

                when (selectedTabId) {
                    TabConstants.ALL_FILES -> {
                        RecordsScreenContent(
                            viewModel = viewModel,
                            mode = Mode.VIEW,
                            selectedItems = selectedItems,
                            onSelectedItemsChange = { items ->
//                            selectedItems.clear()
//                            selectedItems.addAll(items)
                            },
                            openSmartReport = openSmartReport,
                            openRecordViewer = openRecordViewer,
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

private fun syncRecords(
    filterIds: List<String>,
    ownerId: String,
    context: Context,
) {
    val records = Records.getInstance(context = context, token = "")
    records.refreshRecords(context, ownerId, filterIds)
}


object TabConstants {
    const val ALL_FILES = "all_files"
    const val MEDICAL_CASES = "medical_cases"
}
