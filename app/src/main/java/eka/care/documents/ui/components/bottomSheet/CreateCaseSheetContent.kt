package eka.care.documents.ui.components.bottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R
import eka.care.documents.ui.utility.CaseType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CreateCaseSheetContent(
    onDismiss: () -> Unit = {},
    onCreateCase: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var caseName by remember { mutableStateOf("Amit diet") }
    var selectedCaseType by remember { mutableStateOf<CaseType?>(null) }
    var selectedDate by remember { mutableStateOf("08/17/2025") }
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        CreateCaseHeader(onDismiss = onDismiss)
        Spacer(modifier = Modifier.height(16.dp))
        CreateCaseInfoBanner()
        Spacer(modifier = Modifier.height(32.dp))

        CreateCaseNameField(
            caseName = caseName,
            onCaseNameChange = { caseName = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Case Type Dropdown - Updated to use enum
        CreateCaseTypeDropdown(
            selectedCaseType = selectedCaseType?.displayName ?: "", // Convert enum to string
            onTypeSelected = {it}
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Date Field
        CreateCaseDateField(
            selectedDate = selectedDate,
            onDateClick = { showDatePicker = true }
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Create Button
        CreateCaseButton(
            enabled = caseName.isNotBlank() && selectedCaseType != null,
            onClick = {
                selectedCaseType?.let { caseType ->
                    onCreateCase(caseName, caseType.displayName, selectedDate)
                }
                onDismiss()
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
    }

    // Date Picker Dialog
    if (showDatePicker) {
        CreateCaseDatePicker(
            currentDate = selectedDate,
            onDateSelected = { selectedDate = it },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
private fun CreateCaseHeader(
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = EkaTheme.colors.onSurface,
            )
        }

        Text(
            text = "Select or Create your Case",
            style = EkaTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.width(24.dp))
    }
}

@Composable
private fun CreateCaseInfoBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFFBEFCE),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_info_outline),
            contentDescription = "Close",
            tint = EkaTheme.colors.secondary,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "Use a Case to organize all documents related to a medical event, whether it's a doctor visit, hospital admission, illness, or surgery. For easy access in one place.",
            style = EkaTheme.typography.bodySmall,
            color = EkaTheme.colors.onSurface
        )
    }
}

@Composable
private fun CreateCaseNameField(
    caseName: String,
    onCaseNameChange: (String) -> Unit
) {
    OutlinedTextField(
        value = caseName,
        onValueChange = onCaseNameChange,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF6750A4),
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        shape = RoundedCornerShape(4.dp),
        textStyle = TextStyle(
            fontSize = EkaTheme.typography.bodyMedium.fontSize,
            color = EkaTheme.colors.onSurface
        )
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateCaseTypeDropdown(
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
            // Search field with close button
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

            // Divider
            HorizontalDivider(
                color = Color.Gray.copy(alpha = 0.2f),
                thickness = 1.dp
            )

            // Case type options
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

                // Create new case type option
                item {
                    CreateNewCaseTypeItem(
                        searchText = searchText,
                        onClick = {
                            // Handle create new case type
                            onTypeSelected(CaseType.OTHER) // Or handle custom creation
                        }
                    )
                }
            }
        }
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
        // Icon with colored background
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
private fun CreateCaseDateField(
    selectedDate: String,
    onDateClick: () -> Unit
) {


        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDateClick() },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Calendar",
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
            shape = RoundedCornerShape(8.dp),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            )
        )

}

@Composable
private fun CreateCaseButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6750A4),
            contentColor = Color.White,
            disabledContainerColor = Color.Gray.copy(alpha = 0.3f),
            disabledContentColor = Color.Gray
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "Create",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateCaseDatePicker(
    currentDate: String,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                        val date = Date(millis)
                        onDateSelected(formatter.format(date))
                    }
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

// Previews
@Preview(showBackground = true, heightDp = 800)
@Composable
fun CreateCaseContentPreview() {
    MaterialTheme {
        CreateCaseSheetContent()
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