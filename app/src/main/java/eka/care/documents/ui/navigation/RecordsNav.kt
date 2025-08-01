package eka.care.documents.ui.navigation

import eka.care.documents.ui.utility.Mode

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

data class SmartReportNavModel(
    val localId: String? = null,
)