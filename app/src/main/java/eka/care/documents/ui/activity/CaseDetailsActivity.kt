package eka.care.documents.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.eka.ui.theme.EkaTheme
import com.google.gson.Gson
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.screens.CaseDetailsScreen
import eka.care.documents.ui.theme.AppColorScheme
import eka.care.documents.ui.viewmodel.CaseDetailsViewModel
import eka.care.documents.ui.viewmodel.RecordsViewModel
import org.json.JSONObject

class CaseDetailsActivity: ComponentActivity() {

    private val recordsViewModel: RecordsViewModel by viewModels()
    private val caseDetailsViewModel: CaseDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jsonString = intent.getStringExtra(AddRecordParams.PARAMS_KEY)
        if (jsonString.isNullOrEmpty()) {
            Log.e("AddPreviewActivity", "Params JSON is missing!")
            return
        }
        val params = Gson().fromJson(jsonString, JSONObject::class.java)
        val businessId = params.optString(AddRecordParams.BUSINESS_ID.key)
        val ownerId = params.optString(AddRecordParams.OWNER_ID.key)
        val links = params.optString(AddRecordParams.LINKS.key)
        val caseId = params.optString(AddRecordParams.CASE_ID.key)

        setContent {
            EkaTheme(
                colorScheme = AppColorScheme
            ) {
                CaseDetailsScreen(
                    viewModel = recordsViewModel,
                    caseDetailsViewModel = caseDetailsViewModel,
                    caseId = caseId,
                    params = MedicalRecordsNavModel(
                        businessId = businessId,
                        ownerId = ownerId,
                        links = links
                    ),
                    onBackPressed = {
                        finish()
                    },
                    openSmartReport = {

                    },
                    openRecordViewer = {

                    }
                )
            }
        }
    }
}