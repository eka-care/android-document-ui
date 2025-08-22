package eka.care.documents.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.gson.Gson
import com.google.gson.JsonObject
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.screens.CasesScreen
import eka.care.documents.ui.utility.RecordsAction.Companion.navigateToCaseDetails
import eka.care.documents.ui.viewmodel.RecordsViewModel

class CaseListActivity : ComponentActivity() {

    private val recordsViewModel: RecordsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jsonString = intent.getStringExtra(AddRecordParams.PARAMS_KEY)
        if (jsonString.isNullOrEmpty()) {
            Log.e("CaseListActivity", "Params JSON is missing!")
            return
        }
        val params = Gson().fromJson(jsonString, JsonObject::class.java)
        val businessId = params.get(AddRecordParams.BUSINESS_ID.key).asString
        val ownerId = params.get(AddRecordParams.OWNER_ID.key).asString
        val links = params.get(AddRecordParams.LINKS.key).asString

        setContent {
            CasesScreen(
                viewModel = recordsViewModel,
                params = MedicalRecordsNavModel(
                    businessId = businessId,
                    ownerId = ownerId,
                    links = links
                ),
                onCaseItemClick = {
                    navigateToCaseDetails(
                        context = this,
                        params = MedicalRecordsNavModel(
                            businessId = businessId,
                            ownerId = ownerId,
                            links = links
                        ),
                        caseItem = it
                    )
                },
                onBackPressed = {
                    finish()
                }
            )
        }
    }
}