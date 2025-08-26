package eka.care.documents.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import eka.care.records.client.utils.Records
import kotlinx.coroutines.launch

class CaseDetailsViewModel(val app: Application) : AndroidViewModel(app) {
    private val recordsManager = Records.getInstance(
        context = app.applicationContext,
        token = ""
    )

    var caseBottomSheet by mutableStateOf(CaseDetailsOptions.MORE)

    fun deleteCase(businessId: String, caseId: String) {
        viewModelScope.launch {
            recordsManager.deleteEncounter(
                encounterId = caseId,
            )
            recordsManager.syncRecords(businessId)
        }
    }
}

enum class CaseDetailsOptions {
    MORE,
    CASE_DETAILS,
    EDIT_CASE
}