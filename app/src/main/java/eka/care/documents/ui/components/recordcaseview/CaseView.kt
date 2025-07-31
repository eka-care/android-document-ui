package eka.care.documents.ui.components.recordcaseview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R
import eka.care.documents.ui.model.RecordCase

@Composable
fun CaseView(
    cases: List<RecordCase> = emptyList(),
    onCaseItemClick: (RecordCase) -> Unit = {}
){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(cases) { caseItem ->
            RecordCaseItem(
                record = caseItem,
                onClick = { onCaseItemClick(caseItem) }
            )
        }
    }
}


private val sampleCases = listOf(
    RecordCase("Dr. Raghu Gupta", 72, "25 Fri", R.drawable.ic_user_doctor),
    RecordCase("Apollo Hospital", 102, "21 Fri", R.drawable.ic_bed),
    RecordCase("Health Check-up", 5, "20 Wed", R.drawable.ic_stethoscope),
    RecordCase("Home Visit", 3, "18 Mon", R.drawable.ic_house),
    RecordCase("Emergency", 1, "15 Fri", R.drawable.ic_ambulance)
)

@Preview(showBackground = true, name = "With Custom Data")
@Composable
fun CaseViewWithDataPreview() {
    EkaTheme {
        CaseView(cases = sampleCases)
    }
}