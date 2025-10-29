package eka.care.documents.ui.navigation

data class AddRecordPreviewNavModel(
    val pdfUriString: String? = null,
    val imageUris: String? = null,
    val businessId: String,
    val ownerId: String,
    val caseId: String? = null,
    val isAbhaEnabled : Boolean = false,
    val documentTypes: List<MedicalRecordsNavModel.DocumentType>
)