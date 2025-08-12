package eka.care.documents.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R
import eka.care.documents.ui.theme.StyleDictionaryColor
import eka.care.documents.ui.utility.DocumentBottomSheetType
import eka.care.documents.ui.utility.DocumentViewType
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.SortOrder

@Composable
fun RecordSortSection(viewModel: RecordsViewModel, openSheet: () -> Unit) {
    val sortBy = viewModel.sortBy.value
    val documentViewType = viewModel.documentViewType
    val handleSort = {
        viewModel.documentBottomSheetType = DocumentBottomSheetType.DocumentSort
        openSheet.invoke()
    }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AssistChip(
                onClick = handleSort,
                label = {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(color = EkaTheme.colors.onSurfaceVariant)
                            ) {
                                append("Sort by: ")
                            }

                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Medium,
                                    color = EkaTheme.colors.onSurfaceVariant
                                )
                            ) {
                                append(
                                    when (sortBy) {
                                        SortOrder.CREATED_AT_ASC -> "Created at"
                                        SortOrder.CREATED_AT_DSC -> "Created at"
                                        SortOrder.DOC_DATE_ASC -> "Document date"
                                        SortOrder.DOC_DATE_DSC -> "Document date"
                                        SortOrder.UPDATED_AT_ASC -> "Updated at"
                                        SortOrder.UPDATED_AT_DSC -> "Updated at"
                                    }
                                )
                            }
                        }
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Sort options",
                        modifier = Modifier.size(AssistChipDefaults.IconSize),
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = StyleDictionaryColor.schemesPrimaryFixed,
                    labelColor = EkaTheme.colors.surfaceBright,
                    trailingIconContentColor = EkaTheme.colors.onSurfaceVariant
                ),
                border = BorderStroke(1.dp, EkaTheme.colors.primary)
            )

            IconButton(onClick = { viewModel.toggleDocumentViewType() }) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(4.dp),
                    painter = if (documentViewType == DocumentViewType.GridView) {
                        painterResource(R.drawable.ic_list_regular)
                    } else {
                        painterResource(R.drawable.ic_grid_2_sharp_regular)
                    },
                    contentDescription = "Multi View",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}