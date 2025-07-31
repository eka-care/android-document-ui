package eka.care.documents.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun RecordsSearchBar() {
    var expanded by rememberSaveable { mutableStateOf(false) }
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(EkaTheme.colors.surface)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        inputField = {
            SearchBarDefaults.InputField(
                query = "",
                onQueryChange = { },
                onSearch = {

                },
                leadingIcon = {
                    IconButton(onClick = { expanded = false }) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .padding(2.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Multi View",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { expanded = false }) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .padding(2.dp),
                            painter = painterResource(R.drawable.rounded_filter_alt_24),
                            contentDescription = "Multi View",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = {
                    Text("Search in medical cases")
                }
            )
        },
        colors = SearchBarDefaults.colors(
            containerColor = EkaTheme.colors.surfaceContainerHigh
        ),
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {

    }
}