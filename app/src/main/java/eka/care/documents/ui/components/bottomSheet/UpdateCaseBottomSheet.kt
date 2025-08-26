package eka.care.documents.ui.components.bottomSheet

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import eka.care.documents.ui.components.common.BottomSheetContentLayout
import eka.care.documents.ui.state.CasesState
import eka.care.documents.ui.viewmodel.RecordsViewModel

@Composable
fun UpdateCaseBottomSheet(
    businessId: String,
    caseId: String,
    viewModel: RecordsViewModel,
    closeSheet: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getCaseDetails(caseId)
    }
    val state = viewModel.getCaseDetailsState.collectAsState().value
    var caseName by remember { mutableStateOf("Unknown case" ) }
    when(state) {
        is CasesState.Success -> {
            caseName = state.data.firstOrNull()?.name ?: "Unknown case"
            BottomSheetContentLayout(
                modifier = Modifier.wrapContentHeight(),
                title = "Update case",
                height = .7f
            ) {
                CreateCaseSheetContent(
                    isEditable = true,
                    caseType = state.data.firstOrNull()?.type,
                    onDismiss = { },
                    onCreateCase = { newCaseName, caseType ->
                        viewModel.updateCase(
                            businessId = businessId,
                            caseId = caseId,
                            name = newCaseName,
                            type = caseType.id
                        )
                        closeSheet.invoke()
                    },
                    caseName = caseName,
                    onNameChange = {
                        caseName = it
                    }
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
