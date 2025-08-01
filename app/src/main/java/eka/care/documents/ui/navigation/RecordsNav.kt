package eka.care.documents.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import eka.care.records.ui.presentation.screens.Mode
import kotlinx.serialization.Serializable

@Serializable
data class MedicalRecordsNavModel(
    val filterId: String? = null,
    val ownerId: String,
    val patientUUID: String? = null,
    val name: String? = null,
    val age: Int = -1,
    val gen: String? = null,
    val links: String? = null,
    val success: String? = null,
    val isPDFUpload: Boolean = true,
    val mode : Int = Mode.VIEW.ordinal
)

fun NavController.navigateToMedicalRecords(
    filterId: String? = null,
    ownerId: String,
    patientUUID: String? = null,
    name: String? = null,
    age: Int = -1,
    gen: String? = null,
    links: String? = null,
    success: String? = null,
    isPDFUpload: Boolean = true,
    mode: Mode = Mode.VIEW,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(
        MedicalRecordsNavModel(
            filterId = filterId,
            ownerId = ownerId,
            patientUUID = patientUUID,
            name = name,
            age = age,
            gen = gen,
            links = links,
            success = success,
            isPDFUpload = isPDFUpload,
            mode = mode.ordinal
        )
    )
}

@Serializable
data class SmartReportNavModel(
    val localId: String? = null,
)

fun NavController.navigateToSmartReport(
    data: SmartReportNavModel,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(
        data
    ) {
        navOptions()
    }
}
