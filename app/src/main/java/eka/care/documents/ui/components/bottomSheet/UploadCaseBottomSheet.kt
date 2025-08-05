package eka.care.documents.ui.components.bottomSheet

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eka.care.documents.ui.components.common.BottomSheetContentLayout
import eka.care.documents.ui.viewmodel.RecordsViewModel

@Composable
fun UploadCaseBottomSheet(viewModel: RecordsViewModel, caseNane: String) {
    BottomSheetContentLayout(
        modifier = Modifier.wrapContentHeight(),
        height = 0.7f,
        title = "Select or Create your Case"
    ) {
        CreateCaseSheetContent(
            onDismiss = { },
            onCreateCase = { caseName, caseType ->
//                viewModel.createCase(caseName, caseType)
            },
            caseName = caseNane
        )
    }
}
