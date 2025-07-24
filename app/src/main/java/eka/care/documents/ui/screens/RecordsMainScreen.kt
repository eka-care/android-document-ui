package eka.care.documents.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.components.RecordSortSection
import eka.care.documents.ui.components.RecordTabs
import eka.care.documents.ui.components.RecordsHeader
import eka.care.documents.ui.components.recordListView.RecordsListItem
import eka.care.documents.ui.model.TabItem
import eka.care.documents.ui.theme.RecordsColorScheme

@Composable
@Preview
fun RecordsMainScreen() {
    EkaTheme(
        colorScheme = RecordsColorScheme
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            topBar = {
                RecordsHeader()
            },
            content = { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    stickyHeader {
                        RecordTabs(
                            tabs = listOf(
                                TabItem(id = "all_files", title = "All Files", isSelected = true),
                                TabItem(id = "medical_cases", title = "Medical Cases", isSelected = false)
                            )
                        )
                    }
                    item {
                        RecordSortSection(
                            sortBy = "Upload date",
                            onSortByChange = {},
                            onViewModeToggle = {},
                            isGridView = true
                        )
                    }
                }
            }
        )
    }
}