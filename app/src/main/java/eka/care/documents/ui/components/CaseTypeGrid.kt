package eka.care.documents.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.utility.CaseType

@Composable
fun CaseTypeGrid(
    modifier: Modifier = Modifier,
    selectedCaseType: CaseType? = null,
    onCaseTypeSelected: (CaseType) -> Unit = {}
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CaseType.entries.forEach { caseType ->
            val isSelected = selectedCaseType == caseType

            AssistChip(
                onClick = { onCaseTypeSelected(caseType) },
                label = {
                    Text(
                        text = caseType.displayName,
                        style = EkaTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = caseType.iconRes),
                        contentDescription = caseType.displayName,
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (isSelected)
                        EkaTheme.colors.primary.copy(alpha = 0.1f)
                    else
                        EkaTheme.colors.surface,
                    labelColor = if (isSelected)
                        EkaTheme.colors.primary
                    else
                        EkaTheme.colors.onSurface,
                    leadingIconContentColor = if (isSelected)
                        EkaTheme.colors.primary
                    else
                        EkaTheme.colors.onSurface
                ),
                border = if (isSelected) {
                    BorderStroke(1.dp, EkaTheme.colors.primary)
                } else {
                    BorderStroke(1.dp, EkaTheme.colors.outline)
                }
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun CaseTypeGridPreview() {
    var selectedCaseType by remember { mutableStateOf<CaseType?>(null) }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Text(
                text = "Select Case Type",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )

            CaseTypeGrid(
                selectedCaseType = selectedCaseType,
                onCaseTypeSelected = { selectedCaseType = it }
            )

            selectedCaseType?.let { caseType ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "Selected: ${caseType.displayName}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}