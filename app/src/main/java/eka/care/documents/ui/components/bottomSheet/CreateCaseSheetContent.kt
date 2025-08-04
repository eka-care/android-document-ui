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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eka.ui.buttons.EkaButton
import com.eka.ui.buttons.EkaButtonShape
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R
import eka.care.documents.ui.components.CreateCaseTypeDropdown
import eka.care.documents.ui.components.common.TextFieldWrapper
import eka.care.documents.ui.utility.CaseType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CreateCaseSheetContent(
    onDismiss: () -> Unit = {},
    onCreateCase: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var caseName by remember { mutableStateOf("") }
    var selectedCaseType by remember { mutableStateOf<CaseType?>(null) }
    var selectedDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        CreateCaseInfoBanner()

        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CreateCaseNameField(
                caseName = caseName,
                onCaseNameChange = { caseName = it }
            )

            CreateCaseTypeDropdown(
                selectedCaseType = selectedCaseType?.displayName ?: "",
                onTypeSelected = { it }
            )
            CreateCaseDateField(
                selectedDate = selectedDate,
                onDateClick = { showDatePicker = true }
            )
        }

        EkaButton(
            modifier = Modifier.fillMaxWidth(),
            label = "Create",
            shape = EkaButtonShape.SQUARE,
            enabled = caseName.isNotBlank() && selectedCaseType != null,
            onClick = {
                selectedCaseType?.let { caseType ->
                    onCreateCase(caseName, caseType.displayName, selectedDate)
                }
                onDismiss()
            },
        )
        Spacer(modifier = Modifier.height(32.dp))
    }

    if (showDatePicker) {
        CreateCaseDatePicker(
            currentDate = selectedDate,
            onDateSelected = { selectedDate = it },
            onDismiss = { showDatePicker = false }
        )
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
    TextFieldWrapper(
        modifier = Modifier.fillMaxWidth(),
        value = caseName,
        onChange = onCaseNameChange,
        label = "Case Name",
        placeholder = "Enter case name",
        required = true
    )
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
