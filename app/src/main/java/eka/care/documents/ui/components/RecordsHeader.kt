package eka.care.documents.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsHeader(
    text: String,
    showRecordSelection: Boolean = false,
    onSearch: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onSelection: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(2.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Multi View",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = text,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.W500
            )
            IconButton(onClick = onRefresh) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(2.dp),
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = "Multi View",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onSearch) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(2.dp),
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Multi View",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (showRecordSelection) {
                IconButton(onClick = onSelection) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(2.dp),
                        imageVector = Icons.Rounded.Done,
                        contentDescription = "Done",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    )
}