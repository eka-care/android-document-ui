package eka.care.documents.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.components.smartreport.SmartReportViewComponent
import eka.care.documents.ui.screens.RecordViewerScreen
import eka.care.documents.ui.theme.AppColorScheme
import eka.care.documents.ui.viewmodel.RecordPreviewViewModel

class RecordViewerActivity: ComponentActivity() {

    private val viewModel: RecordPreviewViewModel by  viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isSmart = intent.getBooleanExtra(AddRecordParams.IS_SMART.key, false)
        val recordId = intent.getStringExtra(AddRecordParams.RECORD_ID.key)

        if(recordId.isNullOrEmpty()) {
            finish()
            return
        }

        setContent {
            EkaTheme(
                colorScheme = AppColorScheme
            ) {
                if(isSmart) {
                    SmartReportViewComponent(
                        viewModel = viewModel,
                        id = recordId,
                        onBackClick = {
                            finish()
                        },
                        onClick = { _, _ ->
                            finish()
                        }
                    )
                } else {
                    RecordViewerScreen(
                        viewModel = viewModel,
                        id = recordId,
                        onBackClick = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}