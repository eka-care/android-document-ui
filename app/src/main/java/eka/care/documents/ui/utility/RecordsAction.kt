package eka.care.documents.ui.utility

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.pager.PagerState
import com.google.gson.Gson
import eka.care.documents.ui.activity.AddRecordParams
import eka.care.documents.ui.activity.CaseDetailsActivity
import eka.care.documents.ui.activity.CaseListActivity
import eka.care.documents.ui.model.TabItem
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.screens.TabConstants
import eka.care.records.client.model.CaseModel
import org.json.JSONObject

class RecordsAction {
    companion object {
        const val ACTION_SCAN_A_DOCUMENT = "action_scan_a_document"
        const val ACTION_TAKE_PHOTO = "action_take_photo"
        const val ACTION_UPLOAD_PDF = "action_upload_pdf"
        const val ACTION_CHOOSE_FROM_GALLERY = "action_choose_from_gallery"
        const val ACTION_EDIT_DOCUMENT = "action_edit_document"
        const val ACTION_SHARE_DOCUMENT = "action_share_document"
        const val ASSIGN_DOCUMENT_TO_CASE = "assign_document_to_case"
        const val ACTION_DELETE_RECORD = "action_delete_record"
        const val ACTION_CLOSE_SHEET = "action_close_sheet"
        const val ACTION_OPEN_SHEET = "action_open_sheet"
        const val ACTION_OPEN_DELETE_DIALOG = "action_open_delete_dialog"
        const val ACTION_CASE_DETAILS = "action_case_details"
        const val ACTION_EDIT_CASE = "action_edit_case"
        const val ACTION_DELETE_CASE = "action_delete_case"

        fun navigateToCaseList(
            context: Context,
            params: MedicalRecordsNavModel
        ) {
            val paramsJson = JSONObject().apply {
                put(AddRecordParams.BUSINESS_ID.key, params.businessId)
                put(AddRecordParams.OWNER_ID.key, params.ownerId)
                put(AddRecordParams.LINKS.key, params.links)
            }
            Intent(context, CaseListActivity::class.java).apply {
                putExtra(AddRecordParams.PARAMS_KEY, Gson().toJson(paramsJson))
            }.run {
                context.startActivity(this)
            }
        }

        fun navigateToCaseDetails(
            context: Context,
            params: MedicalRecordsNavModel,
            caseItem: CaseModel
        ) {
            val paramsJson = JSONObject().apply {
                put(AddRecordParams.BUSINESS_ID.key, params.businessId)
                put(AddRecordParams.OWNER_ID.key, params.ownerId)
                put(AddRecordParams.LINKS.key, params.links)
                put(AddRecordParams.CASE_ID.key, caseItem.id)
            }
            Intent(context, CaseDetailsActivity::class.java).apply {
                putExtra(AddRecordParams.PARAMS_KEY, Gson().toJson(paramsJson))
            }.run {
                context.startActivity(this)
            }
        }

        fun getTabs(pagerState: PagerState): List<TabItem> {
            return listOf(
                TabItem(
                    id = TabConstants.ALL_FILES.id,
                    title = "All Files",
                    isSelected = pagerState.currentPage == 0
                ),
                TabItem(
                    id = TabConstants.MEDICAL_CASES.id,
                    title = "Medical Cases",
                    isSelected = pagerState.currentPage == 1
                )
            )
        }
    }
}