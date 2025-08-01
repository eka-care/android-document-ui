package eka.care.records.ui.presentation.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import eka.care.doctor.icons.R
import eka.care.doctor.theme.color.DarwinTouchNeutral0
import eka.care.doctor.theme.color.DarwinTouchNeutral1000
import eka.care.doctor.theme.color.DarwinTouchPrimary
import eka.care.doctor.theme.color.DarwinTouchPrimaryLight
import eka.care.doctor.ui.molecule.ButtonWrapper
import eka.care.doctor.ui.molecule.ButtonWrapperType
import eka.care.doctor.ui.molecule.FabButtonWrapper
import eka.care.doctor.ui.molecule.FabButtonWrapperSizeType
import eka.care.doctor.ui.molecule.FabButtonWrapperType
import eka.care.doctor.ui.molecule.IconButtonWrapper
import eka.care.doctor.ui.organism.AppBar
import eka.care.records.client.model.RecordModel
import eka.care.records.client.model.RecordStatus
import eka.care.records.client.utils.MediaPickerManager
import eka.care.records.client.utils.PhotoPickerHost
import eka.care.records.client.utils.Records
import eka.care.records.ui.presentation.activity.AddRecordParams
import eka.care.records.ui.presentation.activity.AddRecordPreviewActivity
import eka.care.records.ui.presentation.components.FileSharing
import eka.care.records.ui.presentation.components.RecordFilter
import eka.care.records.ui.presentation.components.RecordsBottomSheetContent
import eka.care.records.ui.presentation.components.RecordsScreenContent
import eka.care.documents.ui.components.bottomSheet.DocumentBottomSheetType
import eka.care.records.ui.presentation.components.common.SortAndChangeView
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.state.RecordsState
import eka.care.records.ui.presentation.viewmodel.RecordsViewModel
import eka.care.records.ui.utility.RecordsAction
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

enum class Mode {
    VIEW, SELECTION;

    companion object {
        fun valueOf(value: Int): Mode {
            return if (value == SELECTION.ordinal) {
                SELECTION
            } else {
                VIEW
            }
        }
    }
}

@OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun RecordsScreen(
    navData: MedicalRecordsNavModel,
    selectedRecords: ((List<RecordModel>) -> Unit)? = null,
    isUploadEnabled: Boolean? = true,
    onBackClick: () -> Unit,
    openSmartReport: (data: RecordModel) -> Unit = {},
    openRecordViewer: (data: RecordModel) -> Unit = {},
) {
    val context = LocalContext.current
    val viewModel: RecordsViewModel = koinViewModel()
    val scope = rememberCoroutineScope()
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val filterIdsToProcess = mutableListOf<String>().apply {
        if (navData.filterId?.isNotEmpty() == true) {
            add(navData.filterId)
        }
        if (!navData.links.isNullOrBlank()) {
            navData.links.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() && it != navData.filterId }
                .forEach { add(it) }
        }
    }
    val selectedItems = remember { mutableStateListOf<RecordModel>() }

    LaunchedEffect(Unit) {
        syncRecords(
            filterIds = filterIdsToProcess,
            ownerId = navData.ownerId,
            context = context,
        )
        viewModel.fetchRecordsCount(
            filterIds = filterIdsToProcess,
            ownerId = navData.ownerId,
        )
        viewModel.fetchRecords(
            ownerId = navData.ownerId,
            filterIds = filterIdsToProcess,
        )
    }

    LaunchedEffect(cameraPermissionState.status) {
        if (cameraPermissionState.status != PermissionStatus.Granted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(viewModel.documentType.value) {
        viewModel.fetchRecords(
            ownerId = navData.ownerId,
            filterIds = filterIdsToProcess
        )
    }

    val addRecordResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            syncRecords(
                filterIds = filterIdsToProcess,
                ownerId = navData.ownerId,
                context = context
            )
        }
    }

    val mediaPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(3)) { images ->
            val intent = Intent(context, AddRecordPreviewActivity::class.java)
            val paramsJson = JsonObject().apply {
                images.let { addProperty(AddRecordParams.IMAGE_URIS.key, images.joinToString(",")) }
                addProperty(AddRecordParams.FILTER_ID.key, filterIdsToProcess.first())
                addProperty(AddRecordParams.OWNER_ID.key, navData.ownerId)
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
                    addProperty(AddRecordParams.OWNER_ID.key, navData.ownerId)
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
                        addProperty(AddRecordParams.OWNER_ID.key, navData.ownerId)
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
                    addProperty(AddRecordParams.OWNER_ID.key, navData.ownerId)
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

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = "Confirm Delete") },
            text = { Text(text = "Are you sure you want to delete this record?") },
            confirmButton = {
                ButtonWrapper(
                    type = ButtonWrapperType.TEXT,
                    text = "Yes",
                    onClick = {
                        viewModel.cardClickData.value?.let { record ->
                            viewModel.deleteRecord(
                                localId = record.id
                            )
                            syncRecords(
                                filterIds = filterIdsToProcess,
                                ownerId = navData.ownerId,
                                context = context
                            )
                        }
                        showDeleteDialog = false
                    }
                )
            },
            dismissButton = {
                ButtonWrapper(
                    type = ButtonWrapperType.TEXT,
                    text = "No",
                    onClick = {
                        showDeleteDialog = false
                    }
                )
            }
        )
    }

    val dismissBottomSheet = {
        scope.launch {
            sheetState.hide()
            viewModel.documentBottomSheetType = null
        }
    }

    val openSheet = {
        scope.launch {
            sheetState.show()
        }
    }

    if (viewModel.documentBottomSheetType != null) {
        ModalBottomSheet(
            onDismissRequest = {
                dismissBottomSheet()
            },
            sheetState = sheetState,
            containerColor = DarwinTouchNeutral0,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            dragHandle = null
        ) {
            RecordsBottomSheetContent(
                onClick = {
                    when (it.action) {
                        RecordsAction.ACTION_OPEN_SHEET -> {
                            openSheet()
                        }

                        RecordsAction.ACTION_CLOSE_SHEET -> {
                            dismissBottomSheet()
                        }

                        RecordsAction.ACTION_OPEN_DELETE_DIALOG -> {
                            showDeleteDialog = true
                        }
                    }
                },
                onShare = {
                    scope.launch {
                        viewModel.getRecordDetails()
                        viewModel.cardClickData.value?.let { record ->
                            val filePaths = viewModel.cardClickData.value?.files ?: emptyList()
                            if (filePaths.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Syncing data, please wait!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                FileSharing().shareFiles(
                                    context,
                                    filePaths.mapNotNull { file -> file.filePath })
                            }
                        }
                    }
                },
                onEditDocument = {
                    syncRecords(
                        filterIds = filterIdsToProcess,
                        ownerId = navData.ownerId,
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
                params = navData,
                allFilterIds = filterIdsToProcess
            )
        }
    }
    val snackBarHostState = remember { SnackbarHostState() }
    val failedCount =
        (viewModel.getRecordsState.value as? RecordsState.Success)?.data?.filter { it.status == RecordStatus.SYNC_FAILED }?.size
            ?: 0
    LaunchedEffect(failedCount) {
        if (failedCount > 0) {
            delay(100)
            val result = snackBarHostState.showSnackbar(
                message = "$failedCount files failed to upload!",
                actionLabel = "Try again",
                withDismissAction = true,
                duration = SnackbarDuration.Indefinite
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    syncRecords(
                        filterIds = filterIdsToProcess,
                        ownerId = navData.ownerId,
                        context = context
                    )
                }

                SnackbarResult.Dismissed -> {

                }
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                AppBar(
                    borderColor = Color.Transparent,
                    title = navData.name ?: "",
                    subTitle = if (navData.age > 1) {
                        "${navData.gen}, ${navData.age}y"
                    } else {
                        navData.gen ?: ""
                    },
                    navigationIcon = {
                        IconButtonWrapper(
                            onClick = {
                                onBackClick()
                            },
                            icon = R.drawable.ic_arrow_left_regular,
                            iconSize = 16.dp
                        )
                    },
                    actions = {
                        Row(horizontalArrangement = Arrangement.Center) {
                            if (navData.mode == Mode.SELECTION.ordinal) {
                                ButtonWrapper(
                                    type = ButtonWrapperType.TEXT,
                                    text = "Done",
                                    onClick = {
                                        onBackClick()
                                        viewModel.documentBottomSheetType = null
                                        selectedRecords?.invoke(selectedItems.toList())
                                    },
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButtonWrapper(
                                    icon = R.drawable.ic_arrows_rotate_regular,
                                    onClick = {
                                        syncRecords(
                                            filterIds = filterIdsToProcess,
                                            ownerId = navData.ownerId,
                                            context = context
                                        )
                                    },
                                    iconSize = 16.dp
                                )
                            }
                        }
                    }
                )
                RecordFilter()
                if (viewModel.getRecordsState.value is RecordsState.Loading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = DarwinTouchPrimary
                    )
                }
                SortAndChangeView(
                    viewModel = viewModel
                )
            }
        },
        snackbarHost = {
            CustomSnackBarHost(hostState = snackBarHostState)
        },
        floatingActionButton = {
            if (isUploadEnabled == true) {
                FabButtonWrapper(
                    size = FabButtonWrapperSizeType.EXTENDED,
                    type = FabButtonWrapperType.SECONDARY,
                    onClick = {
                        viewModel.documentBottomSheetType =
                            DocumentBottomSheetType.DocumentUpload
                    },
                    title = "Upload",
                    icon = R.drawable.ic_plus_regular
                )
            }
        },
        content = {
            RecordsScreenContent(
                paddingValues = it,
                mode = Mode.valueOf(navData.mode),
                selectedItems = selectedItems,
                onSelectedItemsChange = { items ->
                    selectedItems.clear()
                    selectedItems.addAll(items)
                },
                openSmartReport = openSmartReport,
                openRecordViewer = openRecordViewer,
                onRefresh = {
                    syncRecords(
                        filterIds = filterIdsToProcess,
                        ownerId = navData.ownerId,
                        context = context
                    )
                }
            )
        },
    )
}

@Composable
fun CustomSnackBarHost(hostState: SnackbarHostState) {
    SnackbarHost(
        hostState = hostState,
        snackbar = { snackBarData ->
            Snackbar(
                snackbarData = snackBarData,
                containerColor = DarwinTouchNeutral1000,
                contentColor = Color.White,
                actionColor = DarwinTouchPrimaryLight
            )
        }
    )
}

fun syncRecords(
    filterIds: List<String>,
    ownerId: String,
    context: Context,
) {
    val records = Records.getInstance(context = context, token = "")
    records.refreshRecords(context, ownerId, filterIds)
}
