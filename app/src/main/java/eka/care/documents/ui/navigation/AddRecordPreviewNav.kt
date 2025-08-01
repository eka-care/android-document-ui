package eka.care.documents.ui.navigation

data class AddRecordPreviewNavModel(
    val pdfUriString: String? = null,
    val imageUris: String? = null,
    val filterId: String,
    val ownerId: String
)