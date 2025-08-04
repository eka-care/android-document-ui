package eka.care.documents.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R
@Preview
@Composable
fun SmartTag(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .border(1.dp, EkaTheme.colors.onPrimary, RoundedCornerShape(8.dp))
            .background(EkaTheme.colors.onPrimary, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_solid_star),
            contentDescription = "Custom Sparkle",
            modifier = Modifier.size(16.dp),
            tint = Color.Unspecified
        )
        Text(text = "Smart", style = EkaTheme.typography.labelLarge, color = EkaTheme.colors.onSurface)
    }
}
