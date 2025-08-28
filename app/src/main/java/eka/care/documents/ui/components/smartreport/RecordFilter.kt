package eka.care.documents.ui.components.smartreport

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun RecordFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = text
            )
        },
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected
        ),
        shape = RoundedCornerShape(8.dp)
    )
}