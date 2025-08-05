package eka.care.documents.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eka.care.documents.ui.R
import eka.care.documents.ui.components.common.TextFieldWrapper
import eka.care.documents.ui.utility.CaseType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCaseTypeDropdown(
    selectedCaseType: String,
    onTypeSelected: (String) -> Unit
) {
    var showDropdown by remember { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDropdown = true }
        ) {
            TextFieldWrapper(
                modifier = Modifier.fillMaxWidth(),
                value = selectedCaseType,
                onChange = { },
                label = "Case Type",
                placeholder = "Select case type",
                trailingIcon = R.drawable.ic_solid_dropdown,
                enabled = false,
                required = true
            )
        }

        if (showDropdown) {
            DropdownList(
                onDismiss = {
                    showDropdown = false
                },
                onTypeSelected = { caseType ->
                    onTypeSelected(caseType.displayName)
                    showDropdown = false
                }
            )
        }
    }
}

@Composable
private fun DropdownList(
    onDismiss: () -> Unit,
    onTypeSelected: (CaseType) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
        ) {
            items(CaseType.entries) { caseType ->
                CaseTypeListItem(
                    caseType = caseType,
                    onClick = { onTypeSelected(caseType) }
                )
            }
        }
    }
}

@Composable
private fun CaseTypeListItem(
    caseType: CaseType,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier
            .background(Color.White)
            .clickable { onClick() },
        headlineContent = {
            Text(
                text = caseType.displayName,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingContent = {
            Image(
                painter = painterResource(id = caseType.iconRes),
                contentDescription = caseType.displayName,
                modifier = Modifier.size(24.dp),
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CaseTypeDropdownPreview() {
    var selectedType by remember { mutableStateOf("") }

    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            CreateCaseTypeDropdown(
                selectedCaseType = selectedType,
                onTypeSelected = { selectedType = it }
            )
        }
    }
}