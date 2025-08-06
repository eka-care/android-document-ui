package eka.care.documents.ui.components.recordcaseview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.utility.CaseType
import eka.care.records.client.model.CaseModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun RecordCaseItem(record: CaseModel, onClick: () -> Unit = {}) {
    ListItem(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(
                bounded = true
            ),
            onClick = onClick
        ),
        headlineContent = {
            Text(
                text = record.name,
                style = EkaTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = EkaTheme.colors.onSurface
            )
        },
        supportingContent = {
            Text(
                text = "${record.records.size} Medical record${if (record.records.size != 1) "s" else ""}",
                style = EkaTheme.typography.labelLarge,
                color = EkaTheme.colors.outline
            )
        },
        leadingContent = {
            val caseType = CaseType.fromId(record.type) ?: CaseType.OTHER
            Image(
                modifier = Modifier.size(40.dp),
                painter = painterResource(caseType.iconRes),
                contentDescription = caseType.displayName
            )
        },
        trailingContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val formatter = SimpleDateFormat("dd EEE", Locale.getDefault())
                Text(
                    text = formatter.format(record.createdAt * 1000),
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
        },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        )
    )
}