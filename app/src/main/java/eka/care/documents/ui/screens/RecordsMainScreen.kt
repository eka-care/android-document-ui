package eka.care.documents.ui.screens

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
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
import eka.care.documents.ui.components.recordListView.RecordsListView
import eka.care.documents.ui.components.recordcaseview.CaseView
import eka.care.documents.ui.components.recordgridview.RecordsGridView
import eka.care.documents.ui.components.syncRecords
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.theme.AppColorScheme
import eka.care.documents.ui.utility.DocumentBottomSheetType
import eka.care.documents.ui.utility.DocumentViewType
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

@OptIn(ExperimentalMaterial3Api::class)
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

    BackHandler(enabled = viewModel.searchActive.value) {
        viewModel.disableRecordSearch()
    }

    var searchText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    val onSearchEnable = {
        searchText = ""
        viewModel.removeSearchResults()
        viewModel.enableRecordSearch(
            businessId = params.businessId,
            ownerIds = owners.toList()
        )
        focusRequester.requestFocus()
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

    val handleRecordClick: (record: RecordModel) -> Unit = { record ->
        if (record.isSmart) {
            val intent = Intent(context, RecordViewerActivity::class.java).apply {
                putExtra(AddRecordParams.RECORD_ID.key, record.id)
                putExtra(AddRecordParams.IS_SMART.key, true)
            }
            vitalResultLauncher.launch(intent)
        } else {
            Intent(context, RecordViewerActivity::class.java).apply {
                putExtra(AddRecordParams.RECORD_ID.key, record.id)
                putExtra(AddRecordParams.IS_SMART.key, false)
            }.run {
                context.startActivity(this)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(
                sides = WindowInsetsSides.Top
            ),
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
                            if (pagerState.currentPage == TabConstants.ALL_FILES.id && eka.care.records.client.utils.Document.getConfiguration().enableSearch) {
                                onSearchEnable()
                            } else {
                                navigateToCaseList(context = context, params = params)
                            }
                        },
                        onSelection = {
                            onRecordSelection(selectedItems)
                            onBackPressed()
                        },
                        onRefresh = {
                            Records.getInstance(context = context).syncRecords(
                                businessId = params.businessId
                            )
                        },
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
                                val intent =
                                    Intent(context, RecordViewerActivity::class.java).apply {
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
                                Toast.makeText(
                                    context,
                                    "Record added successfully",
                                    Toast.LENGTH_SHORT
                                )
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

        AnimatedVisibility(
            visible = viewModel.searchActive.value,
            enter = expandVertically(
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                ),
                expandFrom = Alignment.Top,
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = 250,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = shrinkVertically(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                ),
                shrinkTowards = Alignment.Top
            ) + fadeOut(
                animationSpec = tween(
                    durationMillis = 200,
                    easing = FastOutSlowInEasing
                )
            )
        ) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EkaTheme.colors.surface)
                    .focusRequester(focusRequester)
                    .padding(0.dp)
                    .imePadding(),
                inputField = {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(0.dp),
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            viewModel.searchDocuments(
                                query = it
                            )
                        },
                        placeholder = { Text("Search records...") },
                        singleLine = true,
                        leadingIcon = {
                            IconButton(onClick = {
                                viewModel.disableRecordSearch()
                                viewModel.fetchRecords(
                                    businessId = params.businessId,
                                    owners = owners,
                                    caseId = null
                                )
                            }) {
                                Icon(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(2.dp),
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Multi View",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Search
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                },
                colors = SearchBarDefaults.colors(
                    containerColor = Color.White,
                    dividerColor = EkaTheme.colors.surfaceContainerLow,
                ),
                expanded = viewModel.searchActive.value,
                onExpandedChange = { },
            ) {
                when (viewModel.documentViewType) {
                    DocumentViewType.GridView -> RecordsGridView(
                        state = viewModel.searchState.collectAsState().value,
                        documentTypes = params.documentTypes,
                        mode = params.mode,
                        selectedItems = selectedItems,
                        onSelectedItemsChange = {
                            selectedItems.clear()
                            selectedItems.addAll(it)
                        },
                        onRecordClick = {
                            handleRecordClick(it)
                        },
                        onRetry = {

                        },
                        onUploadRecordClick = {
                            viewModel.documentBottomSheetType =
                                DocumentBottomSheetType.DocumentUpload
                        },
                        onMoreOptionsClick = { record ->
                            viewModel.documentBottomSheetType =
                                DocumentBottomSheetType.DocumentOptions
                            viewModel.cardClickData.value = record
                        }
                    )

                    DocumentViewType.ListView -> RecordsListView(
                        state = viewModel.searchState.collectAsState().value,
                        documentTypes = params.documentTypes,
                        onRecordClick = {
                            handleRecordClick(it)
                        },
                        onUploadRecordClick = {
                            viewModel.documentBottomSheetType =
                                DocumentBottomSheetType.DocumentUpload
                        },
                        onMoreOptionsClick = { record ->
                            viewModel.documentBottomSheetType =
                                DocumentBottomSheetType.DocumentOptions
                            viewModel.cardClickData.value = record
                        }
                    )
                }
            }
        }
    }
}

enum class TabConstants(val id: Int) {
    ALL_FILES(0),
    MEDICAL_CASES(1)
}