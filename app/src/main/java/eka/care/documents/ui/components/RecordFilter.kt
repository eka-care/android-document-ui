package eka.care.documents.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.utility.DocumentViewType
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.SortOrder

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecordFilter(
    viewModel: RecordsViewModel,
    documentTypes: List<MedicalRecordsNavModel.DocumentType> = emptyList(),
    onSortClick: () -> Unit,
    onTagsClick: () -> Unit,
    onFilterApplied: () -> Unit
) {
    val documentType by viewModel.documentType
    val documentViewType = viewModel.documentViewType
    val recordsCountByType by viewModel.recordsCountByType.collectAsState()
    val totalCount = recordsCountByType.data.sumOf { it.count ?: 0 }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = EkaTheme.colors.surface
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            stickyHeader {
                IconButton(
                    modifier = Modifier.background(color = EkaTheme.colors.surface),
                    onClick = { viewModel.toggleDocumentViewType() }) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .padding(4.dp),
                        painter = if (documentViewType == DocumentViewType.GridView) {
                            painterResource(R.drawable.ic_grid_2_sharp_regular)
                        } else {
                            painterResource(R.drawable.ic_list_regular)
                        },
                        contentDescription = "Multi View",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            item {
                FilterChip(
                    onClick = onSortClick,
                    label = {
                        Text(
                            text = "Sort by: ${getSortOrderChipText(viewModel.sortBy.value)}",
                            style = EkaTheme.typography.labelLarge
                        )
                    },
                    border = FilterChipDefaults.filterChipBorder(
                        selected = false,
                        enabled = true,
                        borderColor = EkaTheme.colors.outlineVariant,
                        selectedBorderColor = EkaTheme.colors.primary
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.ArrowDropDown,
                            tint = EkaTheme.colors.onSurfaceVariant,
                            modifier = Modifier.size(16.dp),
                            contentDescription = "selected"
                        )
                    },
                    selected = false,
                    shape = RoundedCornerShape(8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.White,
                        labelColor = EkaTheme.colors.onSurfaceVariant
                    )
                )
            }
            item {
                FilterChip(
                    onClick = onTagsClick,
                    label = {
                        Text(
                            text = "Tags",
                            style = EkaTheme.typography.labelLarge
                        )
                    },
                    border = FilterChipDefaults.filterChipBorder(
                        selected = false,
                        enabled = true,
                        borderColor = EkaTheme.colors.outlineVariant,
                        selectedBorderColor = EkaTheme.colors.primary
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.ArrowDropDown,
                            tint = EkaTheme.colors.onSurfaceVariant,
                            modifier = Modifier.size(16.dp),
                            contentDescription = "selected"
                        )
                    },
                    selected = false,
                    shape = RoundedCornerShape(8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.White,
                        labelColor = EkaTheme.colors.onSurfaceVariant
                    )
                )
            }
            item {
                RecordFilterChip(
                    text = "All ($totalCount)",
                    isSelected = documentType == null,
                    onClick = {
                        viewModel.updateDocumentType(null)
                        onFilterApplied()
                    }
                )
            }
            items(recordsCountByType.data) { recordsInfo ->
                val name = documentTypes.firstOrNull { it.id == recordsInfo.documentType }?.name ?: "Unknown"
                RecordFilterChip(
                    text = "$name (${recordsInfo.count})",
                    isSelected = documentType == recordsInfo.documentType,
                    onClick = {
                        viewModel.updateDocumentType(recordsInfo.documentType)
                        onFilterApplied()
                    }
                )
            }
        }
    }
}

@Composable
fun RecordFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                style = EkaTheme.typography.labelLarge
            )
        },
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Default.Check,
                    tint = EkaTheme.colors.onSurfaceVariant,
                    modifier = Modifier.size(16.dp),
                    contentDescription = "selected"
                )
            }
        } else null,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            labelColor = EkaTheme.colors.onSurfaceVariant
        ),
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(8.dp)
    )
}

fun getSortOrderChipText(order: SortOrder): String {
    return when (order) {
        SortOrder.CREATED_AT_ASC -> "Upload date"
        SortOrder.CREATED_AT_DSC -> "Upload date"
        SortOrder.DOC_DATE_ASC -> "Document date"
        SortOrder.DOC_DATE_DSC -> "Document date"
        SortOrder.UPDATED_AT_ASC -> "Updated date"
        SortOrder.UPDATED_AT_DSC -> "Updated date"
    }
}