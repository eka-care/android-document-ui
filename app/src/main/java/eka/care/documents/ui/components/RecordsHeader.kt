package eka.care.documents.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun RecordsSearchBar(
    onSearch: () -> Unit = {},
    onFilter: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50))
                    .clickable { onSearch() }
                    .background(EkaTheme.colors.surface)
                    .background(Color(0xFFE4E7EC))
                    .padding(all = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(8.dp),
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Multi View",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Search in medical cases",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = onFilter) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(2.dp),
                    painter = painterResource(R.drawable.rounded_filter_alt_24),
                    contentDescription = "Multi View",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}