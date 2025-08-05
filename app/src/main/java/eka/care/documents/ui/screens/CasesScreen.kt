package eka.care.documents.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R
import eka.care.documents.ui.components.bottomSheet.UploadCaseBottomSheet
import eka.care.documents.ui.components.recordcaseview.CaseView
import eka.care.documents.ui.model.RecordCase
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.viewmodel.RecordsViewModel
import kotlinx.coroutines.launch

@Composable
fun CasesScreen(viewModel: RecordsViewModel, params: MedicalRecordsNavModel) {
    val sheetState = rememberModalBottomSheetState(Hidden)
    val scope = rememberCoroutineScope()
    var caseName by rememberSaveable { mutableStateOf("") }

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

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            UploadCaseBottomSheet(
                params = params,
                caseNane = caseName,
                viewModel = viewModel,
                closeSheet = { closeSheet.invoke() }
            )
        },
        content = {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(EkaTheme.colors.surface),
                topBar = {
                    CasesSearchBar(onAddNewCase = {
                        caseName = it
                        openSheet.invoke()
                    })
                },
                content = { paddingValues ->
                    CaseView(
                        modifier = Modifier.padding(paddingValues),
                        cases = sampleCases
                    )
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CasesSearchBar(onAddNewCase: (caseName: String) -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(true) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(EkaTheme.colors.surface)
            .padding(all = if (expanded) 0.dp else 16.dp),
        inputField = {
            SearchBarDefaults.InputField(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = {

                },
                leadingIcon = {
                    IconButton(onClick = { expanded = false }) {
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
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = {
                    Text(
                        text = "Search or add your medical cases..."
                    )
                }
            )
        },
        colors = SearchBarDefaults.colors(
            containerColor = EkaTheme.colors.surfaceContainerHigh
        ),
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        CasesSearchScreenContent(searchQuery, onAddNewCase)
    }
}

@Composable
private fun CasesSearchScreenContent(searchQuery: String, onAddNewCase: (caseName: String) -> Unit) {
    if (searchQuery.isNotEmpty()) {
        Column {
            CaseView(
                cases = sampleCases
            )
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onAddNewCase.invoke(searchQuery)
                    },
                headlineContent = {
                    Text(
                        text = "Create new case “\"$searchQuery\"”",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    } else {
        EmptyCaseView()
    }
}

@Composable
private fun EmptyCaseView() {
    Column(
        modifier = Modifier.padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape((50)))
                .background(EkaTheme.colors.surfaceContainerLow)
                .padding(16.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_arrows_rotate_regular),
                contentDescription = "Empty Case",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp, vertical = 16.dp),
            text = "Start typing to find or create your first medical case",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private val sampleCases = listOf(
    RecordCase("Dr. Raghu Gupta", 72, "25 Fri", R.drawable.ic_user_doctor),
    RecordCase("Apollo Hospital", 102, "21 Fri", R.drawable.ic_bed),
    RecordCase("Health Check-up", 5, "20 Wed", R.drawable.ic_stethoscope),
    RecordCase("Home Visit", 3, "18 Mon", R.drawable.ic_house),
    RecordCase("Emergency", 1, "15 Fri", R.drawable.ic_ambulance)
)