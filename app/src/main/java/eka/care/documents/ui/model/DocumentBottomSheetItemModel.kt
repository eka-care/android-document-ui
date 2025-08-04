package eka.care.documents.ui.model

import androidx.annotation.Keep
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Keep
data class DocumentBottomSheetItemModel(
    val itemName: String,
    val itemNameColor: Color = Color.Unspecified,
    val isRecommended: Boolean = false,
    val action: String,
    val leadingIcon: Int,
    val trailingIcon: ImageVector,
    val leadingIconTint: Color = Color.Unspecified
)