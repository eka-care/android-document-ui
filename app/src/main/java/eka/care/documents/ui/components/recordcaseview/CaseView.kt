package eka.care.documents.ui.components.recordcaseview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onCaseItemClick: (CaseModel) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.getCases(
            ownerId = params.ownerId,
            filterId = params.filterId
        )
    }

    val state by viewModel.getCasesState.collectAsState()
    when (state) {
        is CasesState.Loading -> {}
        is CasesState.EmptyState -> {}
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
            }
        }
    }
}