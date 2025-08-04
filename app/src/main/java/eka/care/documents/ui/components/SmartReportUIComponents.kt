package eka.care.documents.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.viewmodel.RecordPreviewViewModel
import eka.care.records.data.remote.dto.response.SmartReport
import eka.care.records.data.remote.dto.response.SmartReportField


@Composable
fun SmartRecordInfo(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            imageVector = Icons.Default.Check,
            contentDescription = "",
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Documented Smart Report",
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            imageVector = Icons.Default.Check,
            contentDescription = "",
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun SmartReportList(viewModel: RecordPreviewViewModel) {
    val filteredList by viewModel.filteredSmartReport.collectAsState()
    LazyColumn {
        items(filteredList) { reportField ->
            SmartReportListComponent(smartReport = reportField)
        }
    }
}

@Composable
fun SmartReportListComponent(smartReport: SmartReportField) {
    val resultEnum = LabParamResult.entries.find { it.value == smartReport.resultId }
    val resultColor = when (resultEnum) {
        LabParamResult.NORMAL -> EkaTheme.colors.onSurface
        LabParamResult.HIGH, LabParamResult.VERY_HIGH, LabParamResult.CRITICALLY_HIGH -> EkaTheme.colors.onSurface
        LabParamResult.LOW, LabParamResult.VERY_LOW, LabParamResult.CRITICALLY_LOW -> EkaTheme.colors.onSurface
        else -> EkaTheme.colors.onSurface
    }

    ListItem(
        modifier = Modifier.clickable(
            onClick = {

            }
        ),
        headlineContent = {
            Text(
                text = smartReport.name ?: "",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        supportingContent = {
            Text(
                text = smartReport.range ?: "NA",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        trailingContent = {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = smartReport.displayResult ?: "",
                    color = resultColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = smartReport.value ?: "",
                )
            }
        },
    )
}

@Composable
fun SmartReportFilter(smartReport: SmartReport?, viewModel: RecordPreviewViewModel) {
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    LaunchedEffect(smartReport) {
        smartReport?.let {
            viewModel.initializeReports(it)
        }
    }

    val listOfFilter = listOf(
        "All Lab Vitals (${viewModel.getFilteredSmartReport(smartReport).size})",
        "Out of Range (${
            viewModel.getFilteredSmartReport(smartReport).count { field ->
                val resultEnum = LabParamResult.entries.find { it.value == field.resultId }
                resultEnum != LabParamResult.NORMAL && resultEnum != LabParamResult.NO_INTERPRETATION_DONE
            }
        })"
    )
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(listOfFilter.size) { index ->
            val filter = when (index) {
                0 -> Filter.ALL
                1 -> Filter.OUT_OF_RANGE
                else -> Filter.ALL
            }
            RecordFilterChip(
                filteredText = listOfFilter[index],
                openSortBySheet = { viewModel.updateFilter(filter, smartReport) }
            )
        }
    }
}

enum class Filter {
    ALL,
    OUT_OF_RANGE
}
