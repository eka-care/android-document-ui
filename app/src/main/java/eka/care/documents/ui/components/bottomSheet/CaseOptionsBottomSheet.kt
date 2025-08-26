package eka.care.documents.ui.components.bottomSheet

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import eka.care.documents.ui.R
import eka.care.documents.ui.components.common.BottomSheetContentLayout
import eka.care.documents.ui.components.common.RecordBottomSheetItem
import eka.care.documents.ui.model.DocumentBottomSheetItemModel
import eka.care.documents.ui.theme.StyleDictionaryColor
import eka.care.documents.ui.utility.RecordsAction

val caseOptionsItems = arrayOf(
    DocumentBottomSheetItemModel(
        itemName = "Case details",
        action = RecordsAction.ACTION_CASE_DETAILS,
        leadingIcon = R.drawable.ic_file_check_regular,
        trailingIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight
    ),
    DocumentBottomSheetItemModel(
        itemName = "Edit case",
        action = RecordsAction.ACTION_EDIT_CASE,
        leadingIcon = R.drawable.ic_edit_case,
        trailingIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight
    ),
    DocumentBottomSheetItemModel(
        itemName = "Archive case",
        itemNameColor = StyleDictionaryColor.colorBackgroundDanger,
        action = RecordsAction.ACTION_DELETE_CASE,
        leadingIcon = R.drawable.ic_trash_regular,
        leadingIconTint = StyleDictionaryColor.colorBackgroundDanger,
        trailingIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight
    )
)

@Composable
fun CaseOptionsBottomSheet(onClick: (String) -> Unit) {
    BottomSheetContentLayout(
        height = 0.12f * caseOptionsItems.size,
        title = "Choose an option",
    ) {
        caseOptionsItems.map { item ->
            RecordBottomSheetItem(item) {
                onClick(item.action)
            }
        }
    }
}