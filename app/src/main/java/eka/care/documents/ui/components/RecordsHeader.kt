package eka.care.documents.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme

@Composable
@Preview
fun RecordsHeader() {
    Column(
        modifier = Modifier.fillMaxWidth().background(EkaTheme.colors.surfaceContainer)
    ) {
        RecordsSearchBar()
        Spacer(Modifier.height(4.dp))
        RecordsTab()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun RecordsSearchBar() {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(EkaTheme.colors.surfaceContainer),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = {

            },
            content = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Search",
                    tint = EkaTheme.colors.onSurface
                )
            }
        )
        SearchBar(
            modifier = Modifier.weight(1f),
            inputField = {
                SearchBarDefaults.InputField(
                    query = "",
                    onQueryChange = {  },
                    onSearch = {

                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search",
                        )
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search") }
                )
            },
            colors = SearchBarDefaults.colors(
                containerColor = EkaTheme.colors.surfaceContainerHigh
            ),
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {

        }
        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = {

            },
            content = {
                Icon(
                    imageVector = Icons.Rounded.Share,
                    contentDescription = "Search",
                    tint = EkaTheme.colors.onSurface
                )
            }
        )
    }
}

@Composable
@Preview
private fun RecordsTab() {

}