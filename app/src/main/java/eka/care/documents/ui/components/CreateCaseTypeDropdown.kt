package eka.care.documents.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eka.care.documents.ui.utility.CaseType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCaseTypeDropdown(
    selectedCaseType: String,
    onTypeSelected: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var showSearchableDropdown by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedCaseType,
        onValueChange = { },
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showSearchableDropdown = true },
        placeholder = {
            Text(
                text = "Select Type",
                color = Color.Gray.copy(alpha = 0.7f)
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Dropdown",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF6750A4),
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        shape = RoundedCornerShape(8.dp)
    )


    if (showSearchableDropdown) {
        SearchableCaseTypeDropdown(
            searchText = searchText,
            onSearchTextChange = { searchText = it },
            onDismiss = {
                showSearchableDropdown = false
                searchText = ""
            },
            onTypeSelected = { caseType ->
                onTypeSelected(caseType.displayName)
                showSearchableDropdown = false
                searchText = ""
            }
        )
    }
}

@Composable
private fun SearchableCaseTypeDropdown(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onTypeSelected: (CaseType) -> Unit
) {
    val filteredCaseTypes = CaseType.entries.filter {
        it.displayName.contains(searchText, ignoreCase = true)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            Color.Gray.copy(alpha = 0.1f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    decorationBox = { innerTextField ->
                        if (searchText.isEmpty()) {
                            Text(
                                text = "Search case types...",
                                color = Color.Gray.copy(alpha = 0.7f),
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            HorizontalDivider(
                color = Color.Gray.copy(alpha = 0.2f),
                thickness = 1.dp
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
            ) {
                items(filteredCaseTypes) { caseType ->
                    CaseTypeItem(
                        caseType = caseType,
                        onClick = { onTypeSelected(caseType) }
                    )
                }

                item {
                    CreateNewCaseTypeItem(
                        searchText = searchText,
                        onClick = {
                            onTypeSelected(CaseType.OTHER)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateNewCaseTypeItem(
    searchText: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Create new case type \"$searchText\"",
            style = TextStyle(
                fontSize = 16.sp,
                color = Color(0xFF6750A4),
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Create new",
            tint = Color(0xFF6750A4),
            modifier = Modifier.size(20.dp)
        )
    }
}


@Composable
private fun CaseTypeItem(
    caseType: CaseType,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            when (caseType) {
                CaseType.DOCTOR_VISIT -> {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = caseType.displayName,
                        modifier = Modifier.size(20.dp)
                    )
                }

                CaseType.HOSPITAL_ADMIT -> {
                    Text(
                        text = "AA",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                }

                CaseType.EMERGENCY -> {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = caseType.displayName,
                        modifier = Modifier.size(20.dp)
                    )
                }

                else -> {

                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = caseType.displayName,
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            )
        )
    }
}

@Preview(showBackground = true, name = "Case Type Item")
@Composable
fun CaseTypeItemPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            CaseTypeItem(
                caseType = CaseType.DOCTOR_VISIT,
                onClick = {}
            )
            CaseTypeItem(
                caseType = CaseType.HOSPITAL_ADMIT,
                onClick = {}
            )
            CaseTypeItem(
                caseType = CaseType.EMERGENCY,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Case Type Dropdown")
@Composable
fun CaseTypeDropdownPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            SearchableCaseTypeDropdown(
                searchText = "D",
                onSearchTextChange = {},
                onDismiss = {},
                onTypeSelected = {}
            )
        }
    }
}