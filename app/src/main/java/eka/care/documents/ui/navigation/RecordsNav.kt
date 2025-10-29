package eka.care.documents.ui.navigation

import eka.care.documents.ui.utility.Mode
import java.io.Serializable

data class MedicalRecordsNavModel(
    val businessId: String,
    val ownerId: String,
    val links: String? = null,
    val ownerName: String = "",
    val documentType: String? = null,
    val triggerUpload: Boolean = false,
    val mode : Mode = Mode.VIEW,
    val isUploadEnabled: Boolean = false,
    val isAbhaEnabled : Boolean = false,
    val documentTypes: List<DocumentType> = emptyList()
) {
    data class DocumentType(
        val id: String,
        val name: String
    ) : Serializable
}