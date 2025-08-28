package eka.care.documents.ui.components.smartreport

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import com.google.gson.Gson
import eka.care.documents.ui.R
import eka.care.documents.ui.state.DocumentPreviewState
import eka.care.documents.ui.utility.FileSharing
import eka.care.documents.ui.viewmodel.RecordPreviewViewModel
import eka.care.records.data.remote.dto.response.SmartReport
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartReportViewComponent(
    viewModel: RecordPreviewViewModel,
    id: String,
    onBackClick: () -> Unit
) {

    LaunchedEffect(id) { viewModel.getDocument(id = id) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = SmartViewTab.SMART_REPORT.ordinal, pageCount = { 2 })
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    val document by viewModel.document.collectAsState()
//    val record = (document as DocumentPreviewState.Success).data
//    if (record.documentDate != null) getDocumentDate(record.documentDate!!) else
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EkaTheme.colors.surface
                ),
                title = {
                    Text(
                        text = "Smart Report",
                        style = EkaTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
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
                        onClick = {
                            FileSharing.handleFileDownload(
                                context = context,
                                uri = selectedUri
                            )
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(R.drawable.ic_download),
                            contentDescription = "More",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(EkaTheme.colors.surface)
                    .padding(it),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                SmartReportTabBar(pagerState = pagerState, onTabSelect = {
                    scope.launch {
                        pagerState.animateScrollToPage(it)
                        viewModel.updateSelectedTab(SmartViewTab.entries[it])
                    }
                })
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color.White),
                    state = pagerState,
                    userScrollEnabled = true,
                    verticalAlignment = Alignment.Top
                ) { index ->
                    when (index) {
                        SmartViewTab.SMART_REPORT.ordinal -> {
                            when (document) {
                                is DocumentPreviewState.Error -> {
                                    val errorMessage =
                                        (document as DocumentPreviewState.Error).message
                                    ErrorState(message = errorMessage)
                                }

                                is DocumentPreviewState.Loading -> {
                                    LoadingState()
                                }

                                is DocumentPreviewState.Success -> {
                                    val resp = (document as DocumentPreviewState.Success).data
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        verticalArrangement = Arrangement.Top,
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        val smartReport = Gson().fromJson(
                                            resp.smartReport,
                                            SmartReport::class.java
                                        )
                                        SmartRecordInfo(onClick = {
                                            scope.launch {
                                                pagerState.scrollToPage(SmartViewTab.ORIGINAL_RECORD.ordinal)
                                            }
                                        })
                                        SmartReportFilter(smartReport, viewModel = viewModel)
                                        SmartReportList(viewModel = viewModel)
                                    }
                                }
                            }
                        }

                        SmartViewTab.ORIGINAL_RECORD.ordinal -> {
                            when (document) {
                                is DocumentPreviewState.Error -> {
                                    val errorMessage =
                                        (document as DocumentPreviewState.Error).message
                                    ErrorState(message = errorMessage)
                                }

                                is DocumentPreviewState.Loading -> LoadingState()
                                is DocumentPreviewState.Success -> {
                                    RecordSuccessState(
                                        record = (document as? DocumentPreviewState.Success),
                                        onUriSelected = { uri -> selectedUri = uri }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        })
}

enum class SmartViewTab(val type: String) {
    SMART_REPORT("SmartReport"),
    ORIGINAL_RECORD("OriginalRecord")
}

enum class LabParamResult(val value: String) {
    CRITICALLY_LOW("sm-4067860500"),
    VERY_LOW("sm-2631771970"),
    LOW("sm-1220479757"),
    BORDERLINE_LOW("sm-5279274814"),
    NORMAL("sm-8146614980"),
    ABNORMAL("sm-5379306527"),
    BORDERLINE_HIGH("sm-5279215230"),
    HIGH("sm-1420480405"),
    VERY_HIGH("sm-2631712380"),
    CRITICALLY_HIGH("sm-4067205096"),
    NO_INTERPRETATION_DONE("sm-5612225938"),
}