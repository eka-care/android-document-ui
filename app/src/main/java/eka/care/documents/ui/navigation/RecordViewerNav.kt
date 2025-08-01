package eka.care.documents.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable


@Serializable
data class RecordViewerNavModel(
    val localId: String,
)

fun NavController.navigateToMedicalRecordViewer(
    data: RecordViewerNavModel,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(
        RecordViewerNavModel(
            localId = data.localId,
        )
    ) {
        navOptions()
    }
}
