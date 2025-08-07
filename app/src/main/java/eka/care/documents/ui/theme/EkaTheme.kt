package eka.care.documents.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun RecordsTheme(
    content: @Composable()() -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        content = content
    )
}