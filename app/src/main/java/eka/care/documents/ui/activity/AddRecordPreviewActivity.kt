package eka.care.documents.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.gson.Gson
import com.google.gson.JsonObject
import eka.care.documents.ui.navigation.AddRecordPreviewNavModel
import eka.care.documents.ui.screens.AddRecordPreviewScreen
import eka.care.documents.ui.viewmodel.RecordsViewModel

enum class AddRecordParams(val key: String) {
    PDF_URI("pdfUriString"),
    IMAGE_URIS("imageUris"),
    FILTER_ID("filterId"),
    CASE_ID("caseId"),
    OWNER_ID("ownerId");

    companion object {
        const val PARAMS_KEY = "params"
    }
}

class AddRecordPreviewActivity : ComponentActivity() {
    private lateinit var params: JsonObject
    private val recordsViewModel: RecordsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jsonString = intent.getStringExtra(AddRecordParams.PARAMS_KEY)
        if (jsonString.isNullOrEmpty()) {
            Log.e("AddPreviewActivity", "Params JSON is missing!")
            return
        }
        params = Gson().fromJson(jsonString, JsonObject::class.java)

        val navData = AddRecordPreviewNavModel(
            pdfUriString = if (params.has(AddRecordParams.PDF_URI.key)) params.get(AddRecordParams.PDF_URI.key).asString else null,
            imageUris = if (params.has(AddRecordParams.IMAGE_URIS.key)) params.get(AddRecordParams.IMAGE_URIS.key).asString else null,
            filterId = params.get(AddRecordParams.FILTER_ID.key).asString,
            ownerId = params.get(AddRecordParams.OWNER_ID.key).asString,
            caseId = params.get(AddRecordParams.CASE_ID.key).asString
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