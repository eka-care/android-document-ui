package eka.care.documents.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.eka.ui.buttons.EkaButton
import com.eka.ui.buttons.EkaButtonShape
import com.eka.ui.buttons.EkaButtonSize
import com.eka.ui.buttons.EkaButtonStyle
import com.eka.ui.buttons.EkaIcon
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.components.RecordsScreenContent
import eka.care.documents.ui.components.syncRecords
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.utility.DocumentBottomSheetType
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.RecordModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseDetailsScreen(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    caseId: String,
    openSmartReport: (data: RecordModel) -> Unit,
    openRecordViewer: (data: RecordModel) -> Unit,
    onBackPressed: () -> Unit = {},
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
    )
}