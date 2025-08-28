package eka.care.documents.ui.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import com.eka.ui.buttons.EkaButton
import com.eka.ui.buttons.EkaButtonShape
import com.eka.ui.buttons.EkaButtonSize
import com.eka.ui.buttons.EkaButtonStyle
import eka.care.documents.ui.components.common.TextFieldWrapper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerWrapper(
    modifier: Modifier = Modifier,
    selectedDate: String,
    label: String = "DOB",
    placeholder: String = "dd/MM/yyyy",
    format: String = "dd/MM/yyyy",
    onDateSelected: (String) -> Unit,
    trailingIcon: Int? = null,
    required: Boolean = false,
    disableFutureDates: Boolean = false,
    datePickerState: DatePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = (if (disableFutureDates) {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= System.currentTimeMillis()
                }
            }
        } else {
            DatePickerDefaults.AllDates
        })
    )
) {
    var showModal by remember { mutableStateOf(false) }

    TextFieldWrapper(
        value = selectedDate,
        onChange = { },
        required = required,
        label = label,
        placeholder = placeholder,
        enabled = false,
        supportingText = " ",
        trailingIcon = trailingIcon,
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModalInput(
            onDateSelected = {
                it?.let {
                    onDateSelected(
                        convertMillisToDate(
                            it,
                            format = format
                        )
                    )
                }
            },
            onDismiss = { showModal = false },
            datePickerState = datePickerState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalInput(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    datePickerState: DatePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            EkaButton(
                label = "OK",
                shape = EkaButtonShape.ROUNDED,
                size = EkaButtonSize.SMALL,
                style = EkaButtonStyle.TEXT,
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                },
            )
        },
        dismissButton = {
            EkaButton(
                label = "Cancel",
                shape = EkaButtonShape.ROUNDED,
                size = EkaButtonSize.SMALL,
                style = EkaButtonStyle.TEXT,
                onClick = {
                    onDismiss()
                },
            )
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

fun convertMillisToDate(millis: Long, format: String = "dd/MM/yyyy"): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(Date(millis))
}