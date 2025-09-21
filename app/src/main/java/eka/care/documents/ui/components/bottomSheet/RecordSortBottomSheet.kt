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
        height = 0.07f * SortOrder.entries.size,
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
        SortOrder.CREATED_AT_ASC -> "Oldest by creation date"
        SortOrder.CREATED_AT_DSC -> "Newest by creation date"
        SortOrder.DOC_DATE_ASC -> "Earliest document date"
        SortOrder.DOC_DATE_DSC -> "Latest document date"
        SortOrder.UPDATED_AT_ASC -> "Least recently updated"
        SortOrder.UPDATED_AT_DSC -> "Most recently updated"
    }
}