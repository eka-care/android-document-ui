package eka.care.documents.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eka.care.documents.ui.R
import eka.care.documents.ui.components.common.TextFieldWrapper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CreateCaseDateField(
    selectedDate: String,
    onDateClick: () -> Unit
) {
    TextFieldWrapper(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDateClick() },
        value = selectedDate,
        onChange = { },
        label = "Date",
        placeholder = "Select date",
        trailingIcon = R.drawable.ic_calendar_,
        enabled = false
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCaseDatePicker(
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
