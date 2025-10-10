package eka.care.documents.ui.components.recordListView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        modifier = Modifier.fillMaxHeight().background(Color.White),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp, start = 12.dp, end = 12.dp)
    ) {
        items(records, key = { it.id }) { record ->
            RecordsListItem(
                record = record,
                onClick = { onClick(record) },
                subtitle = if (record.documentDate != null) getDocumentDate(record.documentDate!!) else null,
                onMoreOptionsClick = { onMoreOptionsClick(record) },
            )
        }
    }
}