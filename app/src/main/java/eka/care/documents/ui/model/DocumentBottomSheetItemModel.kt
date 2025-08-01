package eka.care.documents.ui.model

import androidx.annotation.Keep
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.eka.ui.buttons.EkaIcon

@Keep
data class DocumentBottomSheetItemModel(
    val itemName: String,
    val itemNameColor: Color = DarwinTouchNeutral1000,
    val isRecommended: Boolean = false,
    val itemType: CTA,
    val leadingIcon: Int,
    val trailingIcon: EkaIcon,
    val leadingIconTint: Color = DarwinTouchNeutral800
)