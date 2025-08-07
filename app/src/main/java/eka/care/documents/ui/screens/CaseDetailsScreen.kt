package eka.care.documents.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.eka.ui.buttons.EkaButton
import com.eka.ui.buttons.EkaButtonShape
import com.eka.ui.buttons.EkaButtonSize
import com.eka.ui.buttons.EkaButtonStyle
import eka.care.documents.ui.components.RecordsScreenContent
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
    caseName: String,
    openSmartReport: (data: RecordModel) -> Unit,
    openRecordViewer: (data: RecordModel) -> Unit,
    onBackPressed: () -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = {
                    Text(
                        text = caseName,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
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
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                label = "Add Record to this Case",
                shape = EkaButtonShape.SQUARE,
                size = EkaButtonSize.MEDIUM,
                style = EkaButtonStyle.FILLED,
                onClick = {
                    viewModel.documentBottomSheetType = DocumentBottomSheetType.DocumentUpload
                }
            )
        }
    )
}