package eka.care.documents.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.theme.StyleDictionaryColor

@Preview
@Composable
fun RecordFilterChip(
    modifier: Modifier = Modifier,
    filteredText: String = "Upload date",
    openSortBySheet: () -> Unit = {}
) {
    AssistChip(
        onClick = openSortBySheet,
        label = {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = EkaTheme.typography.labelLarge.fontWeight,
                            color = EkaTheme.colors.primary
                        )
                    ) {
                        append("Sort by: ")
                    }

                    withStyle(
                        style = SpanStyle(
                            fontWeight = EkaTheme.typography.labelLarge.fontWeight,
                            color = EkaTheme.colors.primary
                        )
                    ) {
                        append(filteredText)
                    }
                }
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Sort options",
                modifier = Modifier.size(AssistChipDefaults.IconSize),
            )
        },
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = StyleDictionaryColor.schemesPrimaryFixed,
            labelColor = EkaTheme.colors.surfaceBright,
            trailingIconContentColor = EkaTheme.colors.onSurfaceVariant
        ),
        border = BorderStroke(1.dp, EkaTheme.colors.primary)
    )
}