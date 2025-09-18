package eka.care.documents.ui.components.smartreport

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.theme.StyleDictionaryColor
import eka.care.documents.ui.theme.StyleDictionaryColor.colorDanger500
import eka.care.documents.ui.theme.StyleDictionaryColor.colorNeutral800
import eka.care.documents.ui.viewmodel.RecordPreviewViewModel
import eka.care.records.data.remote.dto.response.SmartReport
import eka.care.records.data.remote.dto.response.SmartReportField


@Composable
@Preview
fun SmartRecordInfo(onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFBEFCE))
            .clickable {
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Info,
            contentDescription = "",
            tint = Color(0xFF8E6807),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Your original document is the most reliable source of information.",
            style = EkaTheme.typography.bodySmall,
            color = Color(0xFF8E6807),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SmartReportList(viewModel: RecordPreviewViewModel, onClick: (id: String, name: String) -> Unit) {
    val filteredList by viewModel.filteredSmartReport.collectAsState()
    LazyColumn {
        items(filteredList) { reportField ->
            SmartReportListComponent(smartReport = reportField, onClick = onClick)
        }
    }
}

@Composable
fun SmartReportListComponent(smartReport: SmartReportField, onClick: (id: String, name: String) -> Unit) {
    val resultEnum = LabParamResult.entries.find { it.value == smartReport.resultId }
    val resultColor = when (resultEnum) {
        LabParamResult.NORMAL -> StyleDictionaryColor.colorSuccess500
        LabParamResult.HIGH, LabParamResult.VERY_HIGH, LabParamResult.CRITICALLY_HIGH -> colorDanger500
        LabParamResult.LOW, LabParamResult.VERY_LOW, LabParamResult.CRITICALLY_LOW -> colorDanger500
        else -> colorNeutral800
    }

    ListItem(
        modifier = Modifier.clickable(
            onClick = {
                onClick(smartReport.vitalId ?: "", smartReport.name ?: "")
            }
        ),
        colors = ListItemDefaults.colors(
            containerColor = Color.White
        ),
        headlineContent = {
            Text(
                text = smartReport.name ?: "",
                color = EkaTheme.colors.onSurface,
                style = EkaTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        supportingContent = {
            Text(
                text = smartReport.range ?: "NA",
                style = EkaTheme.typography.bodySmall,
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
                    style = EkaTheme.typography.bodyLarge,
                    color = resultColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = smartReport.value ?: "",
                    style = EkaTheme.typography.bodySmall,
                )
            }
        }
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
            .background(Color.White)
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
                text = listOfFilter[index],
                isSelected = selectedFilter == filter,
                onClick = { viewModel.updateFilter(filter, smartReport) }
            )
        }
    }
}

enum class Filter {
    ALL,
    OUT_OF_RANGE
}
