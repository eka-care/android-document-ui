package eka.care.records.ui.presentation.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import eka.care.doctor.theme.color.DarwinTouchNeutral1000
import eka.care.doctor.theme.color.DarwinTouchNeutral50
import eka.care.doctor.typography.touchCalloutBold
import eka.care.doctor.ui.atom.IconWrapper
import eka.care.doctor.ui.molecule.IconButtonWrapper
import eka.care.documents.ui.R
import eka.care.records.client.model.SortOrder
import eka.care.documents.ui.utility.DocumentBottomSheetType
import eka.care.documents.ui.utility.DocumentViewType
import eka.care.documents.ui.viewmodel.RecordsViewModel

@Composable
fun SortAndChangeView(viewModel: RecordsViewModel) {
    val sortBy = viewModel.sortBy.value
    val documentViewType = viewModel.documentViewType
    val handleSort = {
        viewModel.documentBottomSheetType = DocumentBottomSheetType.DocumentSort
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarwinTouchNeutral50)
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            onClick = handleSort,
            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = when(sortBy) {
                            SortOrder.CREATED_AT_ASC -> "Created at"
                            SortOrder.CREATED_AT_DSC -> "Created at"
                            SortOrder.DOC_DATE_ASC -> "Document date"
                            SortOrder.DOC_DATE_DSC -> "Document date"
                            SortOrder.UPDATED_AT_ASC -> "Updated at"
                            SortOrder.UPDATED_AT_DSC -> "Updated at"
                        },
                        color = DarwinTouchNeutral1000,
                        style = touchCalloutBold
                    )
                    IconWrapper(
                        modifier = Modifier.size(16.dp).rotate(
                            when (sortBy) {
                                SortOrder.CREATED_AT_ASC -> 0f
                                SortOrder.DOC_DATE_ASC -> 0f
                                SortOrder.UPDATED_AT_ASC -> 0f
                                SortOrder.CREATED_AT_DSC -> 180f
                                SortOrder.DOC_DATE_DSC -> 180f
                                SortOrder.UPDATED_AT_DSC -> 180f
                            }
                        ),
                        icon = R.drawable.ic_arrow_up_regular,
                        contentDescription = "Sort",
                    )
                }
            }
        )
        IconButtonWrapper(
            onClick = {
                viewModel.documentViewType = if (documentViewType == DocumentViewType.GridView) {
                    DocumentViewType.ListView
                } else {
                    DocumentViewType.GridView
                }
            },
            icon = if (documentViewType == DocumentViewType.GridView) {
                R.drawable.ic_list_regular
            } else {
                R.drawable.ic_grid_2_sharp_regular
            },
            contentDescription = if (documentViewType == DocumentViewType.GridView) "list" else "grid"
        )
    }
}