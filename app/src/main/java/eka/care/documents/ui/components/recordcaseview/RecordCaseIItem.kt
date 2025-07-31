package eka.care.documents.ui.components.recordcaseview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R
import eka.care.documents.ui.model.RecordCase

@Composable
fun RecordCaseItem(record: RecordCase, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        record.iconRes?.let { iconRes ->
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = iconRes),
                contentDescription = null,
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = record.title,
                style = EkaTheme.typography.bodyLarge,
                color = EkaTheme.colors.onSurface
            )
            Text(
                text = "${record.recordCount} Medical record${if (record.recordCount != 1) "s" else ""}",
                style = EkaTheme.typography.labelLarge,
                color = EkaTheme.colors.outline
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = record.date,
                style = EkaTheme.typography.labelSmall,
                color = EkaTheme.colors.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Sort options",
                modifier = Modifier.size(AssistChipDefaults.IconSize),
            )
        }
    }
}


@Preview(showBackground = true, name = "Single Item")
@Composable
fun RecordCaseItemSinglePreview() {
    EkaTheme {
        RecordCaseItem(
            record = RecordCase(
                title = "Dr. Raghu Gupta",
                recordCount = 72,
                date = "25 Fri",
                iconRes = R.drawable.ic_solid_star
            )
        )
    }
}