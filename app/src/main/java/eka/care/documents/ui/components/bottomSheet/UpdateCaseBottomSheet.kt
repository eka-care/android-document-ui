package eka.care.documents.ui.components.bottomSheet

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import eka.care.documents.ui.components.common.BottomSheetContentLayout
import eka.care.documents.ui.state.CasesState
import eka.care.documents.ui.viewmodel.RecordsViewModel

@Composable
fun UpdateCaseBottomSheet(
    caseId: String,
    viewModel: RecordsViewModel,
    closeSheet: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getCaseDetails(caseId)
    }
    val state = viewModel.getCaseDetailsState.collectAsState().value
    when(state) {
        is CasesState.Success -> {
            BottomSheetContentLayout(
                modifier = Modifier.wrapContentHeight(),
                title = "Update case",
                height = .7f
            ) {
                CreateCaseSheetContent(
                    onDismiss = { },
                    onCreateCase = { caseName, caseType ->
                        closeSheet.invoke()
                    },
                    caseName = state.data.firstOrNull()?.name ?: "Unknown case"
                )
            }
        }
        is CasesState.Loading -> {
            BottomSheetContentLayout(
                height = 0.1f * 4,
                title = "Case details"
            ) {

            }
        }
        else -> {}
    }
}
