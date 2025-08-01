package eka.care.documents.ui.components.bottomSheet

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import eka.care.documents.ui.R
import eka.care.documents.ui.components.common.BottomSheetContentLayout
import eka.care.documents.ui.components.common.RecordBottomSheetItem
import eka.care.documents.ui.model.DocumentBottomSheetItemModel
import eka.care.documents.ui.theme.StyleDictionaryColor

val documentOptionsItems = arrayOf(
    DocumentBottomSheetItemModel(
        itemName = "Edit document",
//        itemType = CTA(action = RecordsAction.ACTION_EDIT_DOCUMENT),
        leadingIcon = R.drawable.ic_pen_regular,
        trailingIcon = Icons.Default.KeyboardArrowRight
    ),
    DocumentBottomSheetItemModel(
        itemName = "Share document",
//        itemType = CTA(action = RecordsAction.ACTION_SHARE_DOCUMENT),
        leadingIcon = R.drawable.ic_share_nodes_regular,
        trailingIcon = Icons.Default.KeyboardArrowRight
    ),
    DocumentBottomSheetItemModel(
        itemName = "Delete document",
        itemNameColor = StyleDictionaryColor.colorBackgroundDanger,
//        itemType = CTA(action = RecordsAction.ACTION_DELETE_RECORD),
        leadingIcon = R.drawable.ic_trash_regular,
        leadingIconTint = StyleDictionaryColor.colorBackgroundDanger,
        trailingIcon = Icons.Default.KeyboardArrowRight
    )
)

@Composable
fun RecordOptionsBottomSheet(/*onClick: (CTA?) -> Unit*/) {
    BottomSheetContentLayout(
        height = 0.125f * documentOptionsItems.size,
        title = "Choose an option"
    ) {
        documentOptionsItems.map { item ->
            RecordBottomSheetItem(item) {
//                onClick(item.itemType)
            }
        }
    }
}