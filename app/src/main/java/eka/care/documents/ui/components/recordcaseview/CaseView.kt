package eka.care.documents.ui.components.recordcaseview

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.state.CasesState
import eka.care.documents.ui.viewmodel.RecordsViewModel
import eka.care.records.client.model.CaseModel

@Composable
fun CaseView(
    modifier: Modifier = Modifier,
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    onCaseItemClick: (CaseModel) -> Unit = {}
){
    LaunchedEffect(Unit) {
        viewModel.getCases(
            ownerId = params.ownerId,
            filterId = params.filterId
        )
    }

    val state by viewModel.getCasesState.collectAsState()
    when(state) {
        is CasesState.Loading -> {}
        is CasesState.EmptyState -> {}
        is CasesState.Error -> {}
        is CasesState.Success -> {
            LazyColumn(
                modifier = modifier.background(Color.White),
            ) {
                items((state as CasesState.Success).data) { caseItem ->
                    RecordCaseItem(
                        record = caseItem,
                        onClick = { onCaseItemClick(caseItem) }
                    )
                }
            }
        }
    }
}