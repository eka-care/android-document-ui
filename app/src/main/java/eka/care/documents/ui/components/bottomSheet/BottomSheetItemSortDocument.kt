package eka.care.documents.ui.components.bottomSheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetItemSortDocument(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            leadingContent = {
                if (isSelected)
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "",
                        modifier = Modifier.rotate(90f),
                    )
                else
                    Image(
                        painter = ColorPainter(color = Color.Transparent),
                        modifier = Modifier
                            .width(15.dp)
                            .height(12.dp),
                        contentDescription = "",
                    )
            },
            modifier = Modifier.padding(horizontal = 8.dp).height(48.dp),
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
            )
        )
    }
}