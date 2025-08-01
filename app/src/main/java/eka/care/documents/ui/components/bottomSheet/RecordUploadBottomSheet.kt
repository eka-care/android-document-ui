package eka.care.documents.ui.components.bottomSheet

import androidx.compose.runtime.Composable
import eka.care.doctor.icons.R
import eka.care.doctor.ui.organism.BottomSheetContentLayout
import eka.care.records.ui.presentation.components.RecordBottomSheetItem
import eka.care.records.ui.presentation.model.CTA
import eka.care.records.ui.presentation.model.DocumentBottomSheetItemModel
import eka.care.records.ui.utility.RecordsAction

val uploadOptionsItems = arrayOf(
    DocumentBottomSheetItemModel(
        itemName = "Take a photo",
        itemType = CTA(action = RecordsAction.ACTION_TAKE_PHOTO),
        leadingIcon = R.drawable.ic_camera_solid,
        trailingIcon = R.drawable.ic_chevron_right_regular
    ),
    DocumentBottomSheetItemModel(
        itemName = "Scan a document",
        itemType = CTA(action = RecordsAction.ACTION_SCAN_A_DOCUMENT),
        leadingIcon = R.drawable.ic_camera_viewfinder_regular,
        trailingIcon = R.drawable.ic_chevron_right_regular
    ),
    DocumentBottomSheetItemModel(
        itemName = "Choose from gallery",
        itemType = CTA(action = RecordsAction.ACTION_CHOOSE_FROM_GALLERY),
        leadingIcon = R.drawable.ic_image_regular,
        trailingIcon = R.drawable.ic_chevron_right_regular
    ),
    DocumentBottomSheetItemModel(
        itemName = "Upload a PDF",
        itemType = CTA(action = RecordsAction.ACTION_UPLOAD_PDF),
        leadingIcon = R.drawable.ic_file_pdf_solid,
        trailingIcon = R.drawable.ic_chevron_right_regular
    )
)

@Composable
fun RecordUploadBottomSheet(onClick: (CTA?) -> Unit) {
    BottomSheetContentLayout(
        height = 0.125f * uploadOptionsItems.size,
        title = "Add medical records",
    ) {
        uploadOptionsItems.map { item ->
            RecordBottomSheetItem(item) {
                onClick(item.itemType)
            }
        }
    }
}