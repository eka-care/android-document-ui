package eka.care.documents.ui.screens

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.eka.ui.fab.EkaFloatingActionButton
import com.eka.ui.fab.FabColor
import com.eka.ui.fab.FabType
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.activity.AddRecordParams
import eka.care.documents.ui.activity.RecordViewerActivity
import eka.care.documents.ui.components.RecordTabs
import eka.care.documents.ui.components.RecordsHeader
import eka.care.documents.ui.components.RecordsScreenContent
import eka.care.documents.ui.components.recordcaseview.CaseView
import eka.care.documents.ui.components.syncRecords
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.theme.AppColorScheme
import eka.care.documents.ui.utility.DocumentBottomSheetType
import eka.care.documents.ui.utility.Mode
import eka.care.documents.ui.utility.RecordsAction.Companion.getTabs
import eka.care.documents.ui.utility.RecordsAction.Companion.navigateToCaseDetails
import eka.care.documents.ui.utility.RecordsAction.Companion.navigateToCaseList
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.RecordModel
import eka.care.records.client.utils.Records
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsMainScreen(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    onRecordSelection: (List<RecordModel>) -> Unit,
    onBackPressed: () -> Unit = {},
    onVitalClicked: (id: String, name: String) -> Unit,
    activity: Activity
) {
    EkaTheme(
        colorScheme = AppColorScheme
    ) {
        ScreenContent(
            viewModel = viewModel,
            params = params,
            onRecordSelection = onRecordSelection,
            onBackPressed = onBackPressed,
            activity = activity,
            onVitalClicked = onVitalClicked
        )
    }
}

@Composable
private fun ScreenContent(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    onRecordSelection: (List<RecordModel>) -> Unit,
    onBackPressed: () -> Unit,
    onVitalClicked: (id: String, name: String) -> Unit,
    activity: Activity
) {
    val selectedItems = remember { mutableStateListOf<RecordModel>() }
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val vitalResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val id = result.data?.getStringExtra(AddRecordParams.RECORD_ID.key)
                ?: return@rememberLauncherForActivityResult
            val name =
                result.data?.getStringExtra("name") ?: return@rememberLauncherForActivityResult
            onVitalClicked(id, name)
        }
    }

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

    LaunchedEffect(Unit) {
        if (params.triggerUpload) {
            viewModel.documentBottomSheetType = DocumentBottomSheetType.DocumentUpload
        }
    }

    LaunchedEffect(params) {
        viewModel.updateDocumentType(params.documentType)
        viewModel.tags.value = emptyList()
        syncRecords(
            businessId = params.businessId,
            owners = owners,
            context = context
        )
        viewModel.fetchRecordsCount(
            businessId = params.businessId,
            owners = owners,
        )
        viewModel.getTags(
            businessId = params.businessId,
            owners = owners
        )
        viewModel.fetchRecords(
            businessId = params.businessId,
            owners = owners,
            caseId = null
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                RecordsHeader(
                    text = params.ownerName,
                    showRecordSelection = params.mode == Mode.SELECTION,
                    onBackPressed = onBackPressed,
                    onSearch = {
                        navigateToCaseList(context = context, params = params)
                    },
                    onSelection = {
                        onRecordSelection(selectedItems)
                        onBackPressed()
                    },
                    onRefresh = {
                        Records.getInstance(context = context).syncRecords(
                            businessId = params.businessId
                        )
                    }
                )
                RecordTabs(
                    tabs = getTabs(pagerState = pagerState),
                    onTabClick = { tabId ->
                        scope.launch {
                            pagerState.animateScrollToPage(tabId)
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (params.isUploadEnabled) {
                EkaFloatingActionButton(
                    fabType = FabType.NORMAL,
                    fabColor = FabColor.PRIMARY,
                    actionText = if (pagerState.currentPage == 0) "Add Record" else "Add Case",
                    icon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Icon",
                            tint = EkaTheme.colors.onPrimary
                        )
                    },
                    onClick = {
                        if (pagerState.currentPage == TabConstants.ALL_FILES.id) {
                            viewModel.documentBottomSheetType =
                                DocumentBottomSheetType.DocumentUpload
                        } else {
                            navigateToCaseList(context = context, params = params)
                        }
                    }
                )
            }
        },
        content = { paddingValues ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF2F4F7))
                    .padding(paddingValues)
            ) { page ->
                if (page == TabConstants.ALL_FILES.id) {
                    RecordsScreenContent(
                        viewModel = viewModel,
                        params = params,
                        mode = params.mode,
                        selectedItems = selectedItems,
                        onSelectedItemsChange = { items ->
                            selectedItems.clear()
                            selectedItems.addAll(items)
                        },
                        openSmartReport = {
                            val intent = Intent(context, RecordViewerActivity::class.java).apply {
                                putExtra(AddRecordParams.RECORD_ID.key, it.id)
                                putExtra(AddRecordParams.IS_SMART.key, true)
                            }
                            vitalResultLauncher.launch(intent)
                        },
                        openRecordViewer = {
                            Intent(context, RecordViewerActivity::class.java).apply {
                                putExtra(AddRecordParams.RECORD_ID.key, it.id)
                                putExtra(AddRecordParams.IS_SMART.key, false)
                            }.run {
                                context.startActivity(this)
                            }
                        },
                        onRecordAdded = {
                            Toast.makeText(context, "Record added successfully", Toast.LENGTH_SHORT)
                                .show()
                        },
                        activity = activity
                    )
                } else if (page == TabConstants.MEDICAL_CASES.id) {
                    CaseView(
                        viewModel = viewModel,
                        params = params,
                        onCaseItemClick = { caseItem ->
                            navigateToCaseDetails(
                                context = context,
                                params = params,
                                caseItem = caseItem
                            )
                        }
                    )
                }
            }
        }
    )
}

enum class TabConstants(val id: Int) {
    ALL_FILES(0),
    MEDICAL_CASES(1)
}