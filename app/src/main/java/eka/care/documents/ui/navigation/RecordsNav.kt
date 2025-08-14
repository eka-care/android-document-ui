package eka.care.documents.ui.navigation

data class MedicalRecordsNavModel(
    val filterId: String,
    val ownerId: String,
    val links: String? = null,
)

data class SmartReportNavModel(
    val localId: String? = null,
)