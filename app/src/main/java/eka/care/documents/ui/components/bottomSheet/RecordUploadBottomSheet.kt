package eka.care.documents.ui.components.bottomSheet

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import eka.care.documents.ui.R
import eka.care.documents.ui.components.common.BottomSheetContentLayout
import eka.care.documents.ui.components.common.RecordBottomSheetItem
import eka.care.documents.ui.model.DocumentBottomSheetItemModel

val uploadOptionsItems = arrayOf(
    DocumentBottomSheetItemModel(
        itemName = "Take a photo",
//        itemType = CTA(action = RecordsAction.ACTION_TAKE_PHOTO),
        leadingIcon = R.drawable.ic_camera_solid,
        trailingIcon = Icons.Default.KeyboardArrowRight
    ),
    DocumentBottomSheetItemModel(
        itemName = "Scan a document",
//        itemType = CTA(action = RecordsAction.ACTION_SCAN_A_DOCUMENT),
        leadingIcon = R.drawable.ic_camera_viewfinder_regular,
        trailingIcon = Icons.Default.KeyboardArrowRight
    ),
    DocumentBottomSheetItemModel(
        itemName = "Choose from gallery",
//        itemType = CTA(action = RecordsAction.ACTION_CHOOSE_FROM_GALLERY),
        leadingIcon = R.drawable.ic_image_regular,
        trailingIcon = Icons.Default.KeyboardArrowRight
    ),
    DocumentBottomSheetItemModel(
        itemName = "Upload a PDF",
//        itemType = CTA(action = RecordsAction.ACTION_UPLOAD_PDF),
        leadingIcon = R.drawable.ic_file_pdf_solid,
        trailingIcon = Icons.Default.KeyboardArrowRight
    )
)

@Composable
fun RecordUploadBottomSheet(/*onClick: (CTA?) -> Unit*/) {
    BottomSheetContentLayout(
        height = 0.125f * uploadOptionsItems.size,
        title = "Add medical records",
    ) {
        uploadOptionsItems.map { item ->
            RecordBottomSheetItem(item) {
//                onClick(item.itemType)
            }
        }
    }
}