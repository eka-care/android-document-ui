package eka.care.documents.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.gson.Gson
import eka.care.documents.ui.navigation.AddRecordPreviewNavModel
import eka.care.documents.ui.screens.AddRecordPreviewScreen
import eka.care.documents.ui.viewmodel.RecordsViewModel
import org.json.JSONObject

enum class AddRecordParams(val key: String) {
    BUSINESS_ID("businessId"),
    OWNER_ID("ownerId"),
    PDF_URI("pdfUriString"),
    IMAGE_URIS("imageUris"),
    CASE_ID("caseId"),
    RECORD_ID("recordId"),
    IS_SMART("isSmart"),
    LINKS("links");

    companion object {
        const val PARAMS_KEY = "params"
    }
}

class AddRecordPreviewActivity : ComponentActivity() {
    private lateinit var params: JSONObject
    private val recordsViewModel: RecordsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jsonString = intent.getStringExtra(AddRecordParams.PARAMS_KEY)
        if (jsonString.isNullOrEmpty()) {
            Log.e("AddPreviewActivity", "Params JSON is missing!")
            return
        }
        params = Gson().fromJson(jsonString, JSONObject::class.java)

        val navData = AddRecordPreviewNavModel(
            pdfUriString = if (params.has(AddRecordParams.PDF_URI.key)) params.getString(AddRecordParams.PDF_URI.key) else null,
            imageUris = if (params.has(AddRecordParams.IMAGE_URIS.key)) params.getString(AddRecordParams.IMAGE_URIS.key) else null,
            businessId = params.getString(AddRecordParams.BUSINESS_ID.key),
            ownerId = params.getString(AddRecordParams.OWNER_ID.key),
            caseId = if (params.has(AddRecordParams.CASE_ID.key)) params.getString(AddRecordParams.CASE_ID.key) else null
        )

        setContent {
            AddRecordPreviewScreen(
                recordsViewModel = recordsViewModel,
                navData = navData,
                onBackClick = {
                    setResult(RESULT_CANCELED)
                    finish()
                }
            ) {
                setResult(RESULT_OK)
                finish()
            }
        }
    }
}