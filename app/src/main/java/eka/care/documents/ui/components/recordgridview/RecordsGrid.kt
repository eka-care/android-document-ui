package eka.care.documents.ui.components.recordgridview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.utility.Mode
import eka.care.records.client.model.RecordModel

@Composable
fun RecordsGrid(
    records: List<RecordModel>,
    mode : Mode,
    onClick: (record: RecordModel) -> Unit,
    onRetry: (record: RecordModel) -> Unit,
    onMoreOptionsClick: (record: RecordModel) -> Unit,
    selectedItems: SnapshotStateList<RecordModel>? = null,
    onSelectedItemsChange: (List<RecordModel>) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp, start = 12.dp, end = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxHeight().background(EkaTheme.colors.surface)
    ) {
        items(records) { record ->
            RecordsGridItem(
                record = record,
                mode = mode,
                onClick = {
                    if (mode == Mode.SELECTION) {
                        if (selectedItems?.contains(record) == true) {
                            selectedItems.remove(record)
                        } else {
                            selectedItems?.add(record)
                        }
                        onSelectedItemsChange(selectedItems?.toList() ?: emptyList())
                    } else {
                        onClick(record)
                    }
                },
                onRetry = { onRetry(record) },
                onMoreOptionsClick = { onMoreOptionsClick(record) },
                isSelected = selectedItems?.contains(record) == true
            )
        }
    }
}