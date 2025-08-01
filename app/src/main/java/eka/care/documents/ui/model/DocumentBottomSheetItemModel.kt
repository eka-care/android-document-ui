package eka.care.documents.ui.model

import androidx.annotation.Keep
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import eka.care.doctor.theme.color.DarwinTouchNeutral1000
import eka.care.doctor.theme.color.DarwinTouchNeutral800

@Keep
data class DocumentBottomSheetItemModel(
    val itemName: String,
    val itemNameColor: Color = DarwinTouchNeutral1000,
    val isRecommended: Boolean = false,
    val itemType: CTA,
    val leadingIcon: Int,
    val trailingIcon: ImageVector,
    val leadingIconTint: Color = DarwinTouchNeutral800
)