package eka.care.documents.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.gson.Gson
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.screens.CasesScreen
import eka.care.documents.ui.utility.RecordsAction.Companion.navigateToCaseDetails
import eka.care.documents.ui.viewmodel.RecordsViewModel
import org.json.JSONObject

class CaseListActivity : ComponentActivity() {

    private val recordsViewModel: RecordsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jsonString = intent.getStringExtra(AddRecordParams.PARAMS_KEY)
        if (jsonString.isNullOrEmpty()) {
            Log.e("CaseListActivity", "Params JSON is missing!")
            return
        }
        val params = Gson().fromJson(jsonString, JSONObject::class.java)
        val businessId = params.getString(AddRecordParams.BUSINESS_ID.key)
        val ownerId = params.getString(AddRecordParams.OWNER_ID.key)
        val recordId = params.optString(AddRecordParams.RECORD_ID.key).takeIf { it.isNotEmpty() }
        val links = params.optString(AddRecordParams.LINKS.key).takeIf { it.isNotEmpty() }
        val isAbhaEnabled = params.optBoolean(AddRecordParams.IS_ABHA_ENABLED.key, false)
        val documentTypes = if(params.has(AddRecordParams.DOCUMENT_TYPE.key)) {
            val a = params.getString(AddRecordParams.DOCUMENT_TYPE.key)
            Gson().fromJson(a, Array<MedicalRecordsNavModel.DocumentType>::class.java).toList()
        } else  emptyList()

        setContent {
            CasesScreen(
                viewModel = recordsViewModel,
                params = MedicalRecordsNavModel(
                    businessId = businessId,
                    ownerId = ownerId,
                    links = links,
                    documentTypes = documentTypes
                ),
                onCaseItemClick = {
                    if(recordId != null) {
                        recordsViewModel.assignRecordToCase(
                            recordId = recordId,
                            caseId = it.id
                        )
                        navigateToCaseDetails(
                            context = this,
                            params = MedicalRecordsNavModel(
                                businessId = businessId,
                                ownerId = ownerId,
                                links = links,
                                isAbhaEnabled = isAbhaEnabled,
                                documentTypes = documentTypes
                            ),
                            caseItem = it
                        )
                    } else {
                        navigateToCaseDetails(
                            context = this,
                            params = MedicalRecordsNavModel(
                                businessId = businessId,
                                ownerId = ownerId,
                                links = links,
                                documentTypes = documentTypes
                            ),
                            caseItem = it
                        )
                    }
                },
                onBackPressed = {
                    finish()
                }
            )
        }
    }
}