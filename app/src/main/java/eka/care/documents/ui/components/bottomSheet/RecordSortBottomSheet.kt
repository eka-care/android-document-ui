package eka.care.documents.ui.components.bottomSheet

import androidx.compose.runtime.Composable
import eka.care.documents.ui.components.common.BottomSheetContentLayout
import eka.care.records.client.model.SortOrder

@Composable
fun RecordSortBottomSheet(
    selectedSort: SortOrder,
    onCloseClick: () -> Unit,
    onClick: (SortOrder) -> Unit
) {
    BottomSheetContentLayout(
        height = 0.125f * SortOrder.entries.size,
        title = "Sort by"
    ) {
        SortOrder.entries.map { item ->
            BottomSheetItemSortDocument(
                title = getSortOrderTitle(item),
                isSelected = selectedSort == item
            ) {
                onClick(item)
                onCloseClick()
            }
        }
    }
}

fun getSortOrderTitle(order: SortOrder): String {
    return when (order) {
        SortOrder.CREATED_AT_ASC -> "Created at low to high"
        SortOrder.CREATED_AT_DSC -> "Created at high to low"
        SortOrder.DOC_DATE_ASC -> "Document date low to high"
        SortOrder.DOC_DATE_DSC -> "Document date high to low"
        SortOrder.UPDATED_AT_ASC -> "Updated at low to high"
        SortOrder.UPDATED_AT_DSC -> "Updated at high to low"
    }
}