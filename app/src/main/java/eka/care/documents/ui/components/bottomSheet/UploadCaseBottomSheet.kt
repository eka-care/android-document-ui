package eka.care.documents.ui.components.bottomSheet

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eka.care.documents.ui.components.common.BottomSheetContentLayout
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.viewmodel.RecordsViewModel

@Composable
fun UploadCaseBottomSheet(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    closeSheet: () -> Unit,
    caseNane: String
) {
    BottomSheetContentLayout(
        modifier = Modifier.wrapContentHeight(),
        title = "Select or Create your Case",
        height = .75f
    ) {
        CreateCaseSheetContent(
            onDismiss = { },
            onCreateCase = { caseName, caseType ->
                viewModel.createCase(
                    businessId = params.businessId,
                    ownerId = params.ownerId,
                    name = caseName,
                    type = caseType.id
                )
                closeSheet.invoke()
            },
            caseName = caseNane
        )
    }
}
