package eka.care.documents.ui.components.recordListView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import eka.care.documents.ui.utility.DocumentUtility.Companion.getDocumentDate
import eka.care.records.client.model.RecordModel

@Composable
fun RecordsList(
    records: List<RecordModel>,
    onClick: (record: RecordModel) -> Unit,
    onMoreOptionsClick: (record: RecordModel) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(records) { record ->
            RecordsListItem(
                record = record,
                onClick = { onClick(record) },
                subtitle = if (record.documentDate != null) getDocumentDate(record.documentDate!!) else null,
                onMoreOptionsClick = { onMoreOptionsClick(record) },
            )
        }
    }
}