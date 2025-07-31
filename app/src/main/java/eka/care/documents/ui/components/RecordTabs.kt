package eka.care.documents.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.model.TabItem

@Preview
@Composable
fun RecordTabs(
    tabs: List<TabItem> = listOf(
        TabItem(id = "all_files", title = "All Files", isSelected = false),
        TabItem(id = "medical_cases", title = "Medical Cases", isSelected = true)
    ),
    onTabClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val selectedTabIndex = tabs.indexOfFirst { it.isSelected }.takeIf { it >= 0 } ?: 0

    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier.fillMaxWidth(),
        containerColor = EkaTheme.colors.surfaceContainer,
        contentColor = EkaTheme.colors.primary,
        indicator = { tabPositions ->
            if (tabPositions.isNotEmpty() && selectedTabIndex < tabPositions.size) {
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                ) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.width(48.dp),
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