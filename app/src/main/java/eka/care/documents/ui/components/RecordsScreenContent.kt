package eka.care.documents.ui.components

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.eka.ui.buttons.EkaButton
import com.eka.ui.buttons.EkaButtonShape
import com.eka.ui.buttons.EkaButtonSize
import com.eka.ui.buttons.EkaButtonStyle
import com.eka.ui.theme.EkaTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.gson.Gson
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import eka.care.documents.ui.activity.AddRecordParams
import eka.care.documents.ui.activity.AddRecordPreviewActivity
import eka.care.documents.ui.activity.CaseListActivity
import eka.care.documents.ui.components.bottomSheet.RecordsBottomSheetContent
import eka.care.documents.ui.components.recordListView.RecordsListView
import eka.care.documents.ui.components.recordgridview.RecordsGridView
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.utility.DocumentBottomSheetType
import eka.care.documents.ui.utility.DocumentViewType
import eka.care.documents.ui.utility.FileSharing
import eka.care.documents.ui.utility.Mode
import eka.care.documents.ui.utility.RecordsAction
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.RecordModel
import eka.care.records.client.utils.MediaPickerManager
import eka.care.records.client.utils.PhotoPickerHost
import eka.care.records.client.utils.Records
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
fun RecordsScreenContent(
    modifier: Modifier = Modifier,
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    caseId: String? = null,
    mode: Mode = Mode.VIEW,
    selectedItems: SnapshotStateList<RecordModel>? = null,
    onSelectedItemsChange: (List<RecordModel>) -> Unit = {},
    openSmartReport: (data: RecordModel) -> Unit,
    openRecordViewer: (data: RecordModel) -> Unit,
    onRecordAdded: () -> Unit,
    activity: Activity
) {
    val recordsState by viewModel.getRecordsState.collectAsState()
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val pullToRefreshState = rememberPullToRefreshState()
    var showDeleteDialog by remember { mutableStateOf(false) }
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

    val onRefresh: () -> Unit = {
        syncRecords(
            businessId = params.businessId,
            owners = owners,
            context = context
        )
        viewModel.fetchRecords(
            businessId = params.businessId,
            owners = owners,
            caseId = caseId
        )
    }

    LaunchedEffect(cameraPermissionState.status) {
        if (cameraPermissionState.status != PermissionStatus.Granted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getRecordsSyncState(params.businessId)
    }

    val addRecordResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            syncRecords(
                businessId = params.businessId,
                owners = owners,
                context = context
            )
            onRecordAdded()
        }
    }

    val mediaPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(3)) { images ->
            if(images.isNotEmpty()) {
                val intent = Intent(context, AddRecordPreviewActivity::class.java)
                val gson = Gson()
                val paramsJson = JSONObject().apply {
                    images.let { put(AddRecordParams.IMAGE_URIS.key, images.joinToString(",")) }
                    put(AddRecordParams.BUSINESS_ID.key, params.businessId)
                    put(AddRecordParams.OWNER_ID.key, params.ownerId)
                    put(AddRecordParams.CASE_ID.key, caseId)
                    put(AddRecordParams.IS_ABHA_ENABLED.key, params.isAbhaEnabled)
                    put(AddRecordParams.DOCUMENT_TYPE.key, gson.toJson(params.documentTypes))
                }
                intent.putExtra(AddRecordParams.PARAMS_KEY, Gson().toJson(paramsJson))
                addRecordResultLauncher.launch(intent)
            }
        }

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                val intent = Intent(context, AddRecordPreviewActivity::class.java)
                val gson = Gson()
                val paramsJson = JSONObject().apply {
                    put(AddRecordParams.PDF_URI.key, it.toString())
                    put(AddRecordParams.BUSINESS_ID.key, params.businessId)
                    put(AddRecordParams.OWNER_ID.key, params.ownerId)
                    put(AddRecordParams.CASE_ID.key, caseId)
                    put(AddRecordParams.IS_ABHA_ENABLED.key, params.isAbhaEnabled)
                    put(AddRecordParams.DOCUMENT_TYPE.key, gson.toJson(params.documentTypes))
                }
                intent.putExtra(AddRecordParams.PARAMS_KEY, Gson().toJson(paramsJson))
                addRecordResultLauncher.launch(intent)
            }
        }
    )

    val scannerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val gson = Gson()
                val data =
                    GmsDocumentScanningResult.fromActivityResultIntent(result.data)
                data?.pages?.let { pages ->
                    val intent = Intent(context, AddRecordPreviewActivity::class.java)
                    val paramsJson = JSONObject().apply {
                        pages.let {
                            put(
                                AddRecordParams.IMAGE_URIS.key,
                                pages.joinToString(",") { it.imageUri.toString() })
                        }
                        put(AddRecordParams.BUSINESS_ID.key, params.businessId)
                        put(AddRecordParams.OWNER_ID.key, params.ownerId)
                        put(AddRecordParams.CASE_ID.key, caseId)
                        put(AddRecordParams.IS_ABHA_ENABLED.key, params.isAbhaEnabled)
                        put(AddRecordParams.DOCUMENT_TYPE.key, gson.toJson(params.documentTypes))
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
                val gson = Gson()
                val intent = Intent(context, AddRecordPreviewActivity::class.java)
                val paramsJson = JSONObject().apply {
                    put(AddRecordParams.IMAGE_URIS.key, uri.toString())
                    put(AddRecordParams.BUSINESS_ID.key, params.businessId)
                    put(AddRecordParams.OWNER_ID.key, params.ownerId)
                    put(AddRecordParams.CASE_ID.key, caseId)
                    put(AddRecordParams.IS_ABHA_ENABLED.key, params.isAbhaEnabled)
                    put(AddRecordParams.DOCUMENT_TYPE.key, gson.toJson(params.documentTypes))
                }
                intent.putExtra(AddRecordParams.PARAMS_KEY, Gson().toJson(paramsJson))
                addRecordResultLauncher.launch(intent)
            }
        }
    }

    LaunchedEffect(Unit) {
        MediaPickerManager.setHost(object : PhotoPickerHost {
            override fun takePhoto(cameraIntent: Intent, uri: Uri) {
                viewModel.updatePhotoUri(uri)
                cameraLauncher.launch(cameraIntent)
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
                                businessId = params.businessId,
                                owners = owners,
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

    if (viewModel.documentBottomSheetType != null) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                viewModel.documentBottomSheetType = null
            },
            content = {
                RecordsBottomSheetContent(
                    onClick = {
                        when (it) {
                            RecordsAction.ACTION_OPEN_SHEET -> {
                                viewModel.documentBottomSheetType = null
                            }

                            RecordsAction.ACTION_CLOSE_SHEET -> {
                                viewModel.documentBottomSheetType = null
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
                                    FileSharing.shareFiles(
                                        context,
                                        filePaths.mapNotNull { file -> file.filePath })
                                }
                            }
                        }
                    },
                    onEditDocument = {
                        syncRecords(
                            businessId = params.businessId,
                            owners = owners,
                            context = context
                        )
                    },
                    pickPdf = {
                        MediaPickerManager.pickPdf()
                    },
                    scanDocument = {
                        MediaPickerManager.scanDocument(context = activity)
                    },
                    cameraLauncher = {
                        MediaPickerManager.takePhoto(
                            context = context,
                            onPermissionDenied = {
                                navigateToAppSettings(context)
                            }
                        )
                    },
                    pickImagesFromGallery = {
                        MediaPickerManager.pickVisualMedia()
                    },
                    onAssignCase = {
                        val paramsJson = JSONObject().apply {
                            put(AddRecordParams.BUSINESS_ID.key, params.businessId)
                            put(AddRecordParams.OWNER_ID.key, params.ownerId)
                            put(AddRecordParams.CASE_ID.key, caseId)
                            put(AddRecordParams.RECORD_ID.key, viewModel.cardClickData.value?.id)
                            put(AddRecordParams.LINKS.key, params.links)
                        }
                        Intent(context, CaseListActivity::class.java).apply {
                            putExtra(AddRecordParams.PARAMS_KEY, Gson().toJson(paramsJson))
                        }.run {
                            context.startActivity(this)
                        }
                    },
                    viewModel = viewModel,
                    params = params,
                    caseId = caseId,
                    owners = owners
                )
            },
            containerColor = Color.White
        )
    }

    PullToRefreshBox(
        modifier = modifier.background(EkaTheme.colors.surface),
        isRefreshing = viewModel.syncing.collectAsState().value,
        state = pullToRefreshState,
        onRefresh = onRefresh
    ) {
        Column(
            modifier = Modifier.background(EkaTheme.colors.surface)
        ) {
            if (viewModel.syncing.collectAsState().value) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = EkaTheme.colors.primary
                )
            }
            RecordFilter(
                viewModel = viewModel,
                documentTypes = params.documentTypes,
                onSortClick = {
                    viewModel.documentBottomSheetType =
                        DocumentBottomSheetType.DocumentSort
                },
                onTagsClick = {
                    viewModel.documentBottomSheetType =
                        DocumentBottomSheetType.TagsFilter
                },
                onFilterApplied = {
                    viewModel.fetchRecords(
                        businessId = params.businessId,
                        owners = owners,
                        caseId = null
                    )
                }
            )
            when (viewModel.documentViewType) {
                DocumentViewType.GridView -> RecordsGridView(
                    state = recordsState,
                    documentTypes = params.documentTypes,
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
                    documentTypes = params.documentTypes,
                    onRecordClick = handleRecordClick,
                    onUploadRecordClick = handleRecordUploadClick,
                    onMoreOptionsClick = { record ->
                        viewModel.documentBottomSheetType = DocumentBottomSheetType.DocumentOptions
                        viewModel.cardClickData.value = record
                    }
                )
            }
        }
    }
}

fun syncRecords(
    owners: List<String>,
    businessId: String,
    context: Context,
) {
    val records = Records.getInstance(context = context, token = "")
    records.refreshRecords(context, businessId = businessId, ownerIds = owners)
}

fun navigateToAppSettings(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    } catch (e: Exception) {

    }
}