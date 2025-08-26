package eka.care.documents.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R
import eka.care.documents.ui.components.bottomSheet.CreateCaseBottomSheet
import eka.care.documents.ui.components.recordcaseview.CaseView
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.CaseModel
import kotlinx.coroutines.launch

@Composable
fun CasesScreen(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    onBackPressed: () -> Unit,
    onCaseItemClick: (CaseModel) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = Hidden,
        skipHalfExpanded = true
    )
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

    var query by remember { mutableStateOf("") }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            CreateCaseBottomSheet(
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
                    CasesSearchBar(
                        viewModel = viewModel,
                        params = params,
                        onAddNewCase = {
                            caseName = it
                            openSheet.invoke()
                        },
                        onQuery = {
                            query = it
                        },
                        onCaseItemClick = onCaseItemClick,
                        onBackPressed = onBackPressed
                    )
                },
                content = { paddingValues ->
                    CaseView(
                        modifier = Modifier.padding(paddingValues),
                        viewModel = viewModel,
                        onCaseItemClick = onCaseItemClick,
                        query = query,
                        params = params
                    )
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CasesSearchBar(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    onQuery: (String) -> Unit,
    onAddNewCase: (caseName: String) -> Unit,
    onCaseItemClick: (CaseModel) -> Unit,
    onBackPressed: () -> Unit
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(EkaTheme.colors.surface)
            .padding(0.dp),
        inputField = {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(0.dp),
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    onQuery.invoke(searchQuery)
                },
                placeholder = { Text("Search or add your medical cases...") },
                singleLine = true,
                leadingIcon = {
                    IconButton(onClick = onBackPressed) {
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
            containerColor = EkaTheme.colors.surfaceContainerHigh,
            dividerColor = EkaTheme.colors.surfaceContainerLow,
        ),
        expanded = true,
        onExpandedChange = { },
    ) {
        CasesSearchScreenContent(
            searchQuery = searchQuery,
            viewModel = viewModel,
            params = params,
            onAddNewCase = onAddNewCase,
            onCaseItemClick = onCaseItemClick
        )
    }
}

@Composable
private fun CasesSearchScreenContent(
    searchQuery: String,
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    onAddNewCase: (caseName: String) -> Unit,
    onCaseItemClick: (CaseModel) -> Unit
) {
    if (searchQuery.isNotEmpty()) {
        CaseView(
            modifier = Modifier.fillMaxWidth(),
            viewModel = viewModel,
            onCaseItemClick = onCaseItemClick,
            onAddNewCase = onAddNewCase,
            query = searchQuery,
            params = params
        )
    } else {
        EmptyCaseView()
    }
}

@Composable
@Preview
private fun EmptyCaseView() {
    Column(
        modifier = Modifier
            .background(EkaTheme.colors.surface)
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape((50)))
                .background(Color.White)
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