package eka.care.documents.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import eka.care.documents.ui.components.RecordsScreenContent
import eka.care.documents.ui.utility.Mode
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.RecordModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseDetailsScreen(
    viewModel: RecordsViewModel,
    caseId: String,
    ownerId: String,
    filterId: String? = null,
    onBackPressed: () -> Unit = {},
) {
    val selectedItems = remember { mutableStateListOf<RecordModel>() }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = {
                    Text(
                        text = "Preview",
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
                viewModel = viewModel,
                mode = Mode.VIEW,
                ownerId = ownerId,
                filterIdsToProcess = listOfNotNull(filterId),
                selectedItems = selectedItems,
                onSelectedItemsChange = { items ->
//                            selectedItems.clear()
//                            selectedItems.addAll(items)
                },
                openSmartReport = {

                },
                openRecordViewer = {

                },
                openSheet = {

                },
                onRefresh = {

                }
            )
        }
    )
}