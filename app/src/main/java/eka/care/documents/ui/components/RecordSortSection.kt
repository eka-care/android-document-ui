package eka.care.documents.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eka.care.documents.ui.R

@Composable
fun RecordSortSection(
    sortBy: String,
    openSortBySheet: () -> Unit,
    onViewModeToggle: () -> Unit,
    isGridView: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RecordFilterChip(
                filteredText = sortBy,
                openSortBySheet = openSortBySheet
            )

            IconButton(onClick = onViewModeToggle) {
                Icon(
                    modifier = Modifier.size(24.dp).padding(4.dp),
                    painter = if (isGridView) painterResource(id = R.drawable.ic_list_regular) else painterResource(
                        id = R.drawable.ic_grid_2_sharp_regular
                    ),
                    contentDescription = "Multi View",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}