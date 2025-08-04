package eka.care.documents.ui.components.bottomSheet

import androidx.compose.runtime.Composable
import eka.care.documents.ui.components.common.BottomSheetContentLayout

@Composable
fun UploadCaseBottomSheet(
) {
    BottomSheetContentLayout(
        title = "Select or Create your Case"
    ) {
        CreateCaseSheetContent(
            onDismiss = { },
            onCreateCase = { caseName, caseType, date -> }
        )
    }
}
