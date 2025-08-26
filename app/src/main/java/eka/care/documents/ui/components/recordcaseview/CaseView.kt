package eka.care.documents.ui.components.recordcaseview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.state.CasesState
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.CaseModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CaseView(
    modifier: Modifier = Modifier,
    query: String = "",
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    onCaseItemClick: (CaseModel) -> Unit = {},
    onAddNewCase: ((caseName: String) -> Unit)? = null
) {
    LaunchedEffect(Unit) {
        viewModel.getCases(
            businessId = params.businessId,
            ownerId = params.ownerId
        )
    }

    val state by viewModel.getCasesState.collectAsState()
    when (state) {
        is CasesState.Loading -> {}
        is CasesState.EmptyState -> {
            if (query.isNotEmpty()) {
                NewCasePrompt(
                    query = query,
                    onAddNewCase = onAddNewCase
                )
            }
        }

        is CasesState.Error -> {}
        is CasesState.Success -> {
            val cases =
                if (state is CasesState.Success) (state as CasesState.Success).data else emptyList()
            val filteredCases = if (query.isBlank()) {
                cases
            } else {
                cases.filter { case ->
                    case.name.contains(query, ignoreCase = true)
                }
            }
            val groupedData: Map<String, List<CaseModel>> =
                filteredCases
                    .sortedByDescending { it.createdAt }
                    .groupBy {
                        val caseDate = Date(it.createdAt * 1000)
                        val caseCalendar = Calendar.getInstance().apply { time = caseDate }
                        val currentCalendar = Calendar.getInstance()
                        if (caseCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                            caseCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
                        ) {
                            "This Month"
                        } else {
                            SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(caseDate)
                        }
                    }
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                groupedData.toList().forEachIndexed { index, (month, cases) ->
                    stickyHeader {
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = month,
                                    style = EkaTheme.typography.titleSmall,
                                    color = EkaTheme.colors.onSurfaceVariant
                                )
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.White
                            )
                        )
                    }
                    items(cases) { caseItem ->
                        RecordCaseItem(
                            record = caseItem,
                            onClick = { onCaseItemClick(caseItem) }
                        )
                    }
                    if (index < groupedData.size - 1) {
                        item {
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp, horizontal = 16.dp),
                                color = EkaTheme.colors.outlineVariant,
                                thickness = 1.dp
                            )
                        }
                    }
                }
                if (query.isNotEmpty()) {
                    item {
                        NewCasePrompt(
                            query = query,
                            onAddNewCase = onAddNewCase
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NewCasePrompt(
    query: String = "",
    onAddNewCase: ((caseName: String) -> Unit)? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onAddNewCase?.invoke(query)
                keyboardController?.hide()
            },
        headlineContent = {
            Text(
                text = "Create a new case “$query”",
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