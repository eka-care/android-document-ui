package eka.care.documents.ui.components.bottomSheet

import androidx.compose.runtime.Composable
import eka.care.doctor.records.R
import eka.care.doctor.theme.color.DarwinTouchRed
import eka.care.doctor.ui.organism.BottomSheetContentLayout
import eka.care.records.ui.presentation.components.RecordBottomSheetItem
import eka.care.records.ui.presentation.model.CTA
import eka.care.records.ui.presentation.model.DocumentBottomSheetItemModel
import eka.care.records.ui.utility.RecordsAction

val documentOptionsItems = arrayOf(
    DocumentBottomSheetItemModel(
        itemName = "Edit document",
        itemType = CTA(action = RecordsAction.ACTION_EDIT_DOCUMENT),
        leadingIcon = R.drawable.ic_pen_regular,
        trailingIcon = R.drawable.ic_chevron_right_regular
    ),
    DocumentBottomSheetItemModel(
        itemName = "Share document",
        itemType = CTA(action = RecordsAction.ACTION_SHARE_DOCUMENT),
        leadingIcon = R.drawable.ic_share_nodes_regular,
        trailingIcon = R.drawable.ic_chevron_right_regular
    ),
    DocumentBottomSheetItemModel(
        itemName = "Delete document",
        itemNameColor = DarwinTouchRed,
        itemType = CTA(action = RecordsAction.ACTION_DELETE_RECORD),
        leadingIcon = R.drawable.ic_trash_regular,
        leadingIconTint = DarwinTouchRed,
        trailingIcon = R.drawable.ic_chevron_right_regular
    )
)

@Composable
fun RecordOptionsBottomSheet(onClick: (CTA?) -> Unit) {
    BottomSheetContentLayout(
        height = 0.125f * documentOptionsItems.size,
        title = "Choose an option"
    ) {
        documentOptionsItems.map { item ->
            RecordBottomSheetItem(item) {
                onClick(item.itemType)
            }
        }
    }
}