package eka.care.documents.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.model.TabItem

@Preview
@Composable
fun RecordTabs(
    modifier: Modifier = Modifier,
    tabs: List<TabItem> = listOf(
        TabItem(id = 0, title = "All Files", isSelected = false),
        TabItem(id = 1, title = "Medical Cases", isSelected = true)
    ),
    onTabClick: (Int) -> Unit = {}
) {
    val selectedTabIndex = tabs.indexOfFirst { it.isSelected }.takeIf { it >= 0 } ?: 0

    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier.fillMaxWidth(),
        containerColor = EkaTheme.colors.surface,
        divider = {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = EkaTheme.colors.outlineVariant,
                thickness = 1.dp
            )
        },
        indicator = { tabPositions ->
            if (tabPositions.isNotEmpty() && selectedTabIndex < tabPositions.size) {
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                ) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.width(90.dp).clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)),
                        height = 3.dp,
                        color = EkaTheme.colors.primary
                    )
                }
            }
        }
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = tab.isSelected,
                onClick = { onTabClick(tab.id) },
                text = {
                    Text(
                        text = tab.title,
                        style = EkaTheme.typography.titleSmall,
                        color = if (tab.isSelected) {
                            EkaTheme.colors.primary
                        } else {
                            EkaTheme.colors.onSurfaceVariant
                        }
                    )
                }
            )
        }
    }
}