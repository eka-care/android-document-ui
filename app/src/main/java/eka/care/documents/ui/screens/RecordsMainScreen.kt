package eka.care.documents.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
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
import com.google.gson.Gson
import com.google.gson.JsonObject
import eka.care.documents.ui.activity.AddRecordParams
import eka.care.documents.ui.activity.CaseDetailsActivity
import eka.care.documents.ui.activity.CaseListActivity
import eka.care.documents.ui.components.RecordTabs
import eka.care.documents.ui.components.RecordsScreenContent
import eka.care.documents.ui.components.RecordsSearchBar
import eka.care.documents.ui.components.recordcaseview.CaseView
import eka.care.documents.ui.components.syncRecords
import eka.care.documents.ui.model.TabItem
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.theme.AppColorScheme
import eka.care.documents.ui.utility.DocumentBottomSheetType
import eka.care.documents.ui.utility.Mode
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.CaseModel
import eka.care.records.client.model.RecordModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsMainScreen(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    openSmartReport: (data: RecordModel) -> Unit,
    openRecordViewer: (data: RecordModel) -> Unit
) {
    EkaTheme(
        colorScheme = AppColorScheme
    ) {
        ScreenContent(
            viewModel = viewModel,
            params = params,
            openSmartReport = openSmartReport,
            openRecordViewer = openRecordViewer
        )
    }
}

@Composable
private fun ScreenContent(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    openSmartReport: (data: RecordModel) -> Unit = {},
    openRecordViewer: (data: RecordModel) -> Unit = {},
) {
    val selectedItems = remember { mutableStateListOf<RecordModel>() }
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()
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

    LaunchedEffect(params) {
        syncRecords(
            businessId = params.businessId,
            owners = owners,
            context = context
        )
        viewModel.fetchRecordsCount(
            businessId = params.businessId,
            owners = owners,
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
            .background(EkaTheme.colors.surface),
        topBar = {
            Column {
                RecordsSearchBar()
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
                        viewModel.documentBottomSheetType = DocumentBottomSheetType.DocumentUpload
                    } else {
                        val paramsJson = JsonObject().apply {
                            addProperty(AddRecordParams.BUSINESS_ID.key, params.businessId)
                            addProperty(AddRecordParams.LINKS.key, params.links)
                            addProperty(AddRecordParams.OWNER_ID.key, params.ownerId)
                        }
                        Intent(context, CaseListActivity::class.java).apply {
                            putExtra(AddRecordParams.PARAMS_KEY, Gson().toJson(paramsJson))
                        }.run {
                            context.startActivity(this)
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
            ) { page ->
                if (page == 0) {
                    RecordsScreenContent(
                        viewModel = viewModel,
                        params = params,
                        mode = Mode.VIEW,
                        selectedItems = selectedItems,
                        onSelectedItemsChange = { items ->

                        },
                        openSmartReport = openSmartReport,
                        openRecordViewer = openRecordViewer,
                    )
                } else if (page == 1) {
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

private fun navigateToCaseDetails(
    context: Context,
    params: MedicalRecordsNavModel,
    caseItem: CaseModel
) {
    val paramsJson = JsonObject().apply {
        addProperty(AddRecordParams.BUSINESS_ID.key, params.businessId)
        addProperty(AddRecordParams.OWNER_ID.key, params.ownerId)
        addProperty(AddRecordParams.LINKS.key, params.links)
        addProperty(AddRecordParams.CASE_ID.key, caseItem.id)
    }
    Intent(context, CaseDetailsActivity::class.java).apply {
        putExtra(AddRecordParams.PARAMS_KEY, Gson().toJson(paramsJson))
    }.run {
        context.startActivity(this)
    }
}

private fun getTabs(pagerState: PagerState): List<TabItem> {
    return listOf(
        TabItem(
            id = TabConstants.ALL_FILES.id,
            title = "All Files",
            isSelected = pagerState.currentPage == 0
        ),
        TabItem(
            id = TabConstants.MEDICAL_CASES.id,
            title = "Medical Cases",
            isSelected = pagerState.currentPage == 1
        )
    )
}

enum class TabConstants(val id: Int) {
    ALL_FILES(0),
    MEDICAL_CASES(1)
}