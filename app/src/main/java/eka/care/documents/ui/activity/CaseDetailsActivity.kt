package eka.care.documents.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.eka.ui.theme.EkaTheme
import com.google.gson.Gson
import com.google.gson.JsonObject
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.screens.CaseDetailsScreen
import eka.care.documents.ui.theme.AppColorScheme
import eka.care.documents.ui.viewmodel.CaseDetailsViewModel
import eka.care.documents.ui.viewmodel.RecordsViewModel

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
        val params = Gson().fromJson(jsonString, JsonObject::class.java)
        val businessId = params.get(AddRecordParams.BUSINESS_ID.key).asString
        val ownerId = params.get(AddRecordParams.OWNER_ID.key).asString
        val links = params.get(AddRecordParams.LINKS.key).asString
        val caseId = params.get(AddRecordParams.CASE_ID.key).asString

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