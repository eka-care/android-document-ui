package eka.care.documents.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

@Serializable
data class AddRecordPreviewNavModel(
    val pdfUriString: String? = null,
    val imageUris: String? = null,
    val filterId: String,
    val ownerId: String
)

fun NavController.navigateToAddRecordPreview(
    data: AddRecordPreviewNavModel,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(
        AddRecordPreviewNavModel(
            pdfUriString = data.pdfUriString,
            imageUris = data.imageUris,
            filterId = data.filterId,
            ownerId = data.ownerId
        )
    ) {
        navOptions()
    }
}