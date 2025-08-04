package eka.care.documents.ui.components.bottomSheet

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eka.care.documents.ui.components.common.BottomSheetContentLayout

@Composable
fun UploadCaseBottomSheet(
) {
    BottomSheetContentLayout(
        modifier = Modifier.wrapContentHeight(),
        title = "Select or Create your Case"
    ) {
        CreateCaseSheetContent(
            onDismiss = { },
            onCreateCase = { caseName, caseType, date -> }
        )
    }
}
