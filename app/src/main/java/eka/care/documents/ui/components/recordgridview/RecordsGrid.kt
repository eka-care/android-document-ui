package eka.care.documents.ui.components.recordgridview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.unit.dp
import eka.care.records.client.model.RecordModel

@Composable
fun RecordsGrid(
    records: List<RecordModel>,
    mode : Mode,
    onClick: (record: RecordModel) -> Unit,
    onRetry: (record: RecordModel) -> Unit,
    onMoreOptionsClick: (record: RecordModel) -> Unit,
    selectedItems: SnapshotStateList<RecordModel>,
    onSelectedItemsChange: (List<RecordModel>) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(top = 0.dp, bottom = 80.dp, start = 12.dp, end = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = background(DarwinTouchNeutral50)
            .fillMaxWidth()
    ) {
        items(records) { record ->
            RecordsGridItem(
                record = record,
                mode = mode,
                onClick = {
                    if (mode == Mode.SELECTION) {
                        if (selectedItems.contains(record)) {
                            selectedItems.remove(record)
                        } else {
                            selectedItems.add(record)
                        }
                        onSelectedItemsChange(selectedItems.toList())
                    } else {
                        onClick(record)
                    }
                },
                onRetry = { onRetry(record) },
                onMoreOptionsClick = { onMoreOptionsClick(record) },
                isSelected = selectedItems.contains(record)
            )
        }
    }
}