package eka.care.documents.ui.components.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DatePickerWrapper(
//    modifier: Modifier = Modifier,
//    selectedDate: String,
//    label: String = "DOB",
//    placeholder: String = "dd/MM/yyyy",
//    format: String = "dd/MM/yyyy",
//    onDateSelected: (String) -> Unit,
//    trailingIcon: Int? = null,
//    required: Boolean = false,
//    disableFutureDates: Boolean = false,
//    datePickerState: DatePickerState = rememberDatePickerState(
//        initialDisplayMode = DisplayMode.Picker,
//        selectableDates = (if (disableFutureDates) {
//            object : SelectableDates {
//                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
//                    return utcTimeMillis <= System.currentTimeMillis()
//                }
//            }
//        } else {
//            DatePickerDefaults.AllDates
//        })
//    )
//) {
//    var showModal by remember { mutableStateOf(false) }
//
//    TextFieldWrapper(
//        value = selectedDate,
//        onChange = { },
//        required = required,
//        label = label,
//        placeholder = placeholder,
//        enabled = false,
//        supportingText = " ",
//        trailingIcon = trailingIcon,
//        colors = OutlinedTextFieldDefaults.colors(
//            disabledBorderColor = DarwinTouchNeutral200,
//            disabledTextColor = DarwinTouchNeutral1000,
//            disabledTrailingIconColor = DarwinTouchNeutral800,
//            disabledPlaceholderColor = DarwinTouchNeutral800,
//            disabledLabelColor = DarwinTouchNeutral800
//        ),
//        modifier = modifier
//            .fillMaxWidth()
//            .pointerInput(selectedDate) {
//                awaitEachGesture {
//                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
//                    // in the Initial pass to observe events before the text field consumes them
//                    // in the Main pass.
//                    awaitFirstDown(pass = PointerEventPass.Initial)
//                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
//                    if (upEvent != null) {
//                        showModal = true
//                    }
//                }
//            }
//    )
//
//    if (showModal) {
//        DatePickerModalInput(
//            onDateSelected = {
//                it?.let {
//                    onDateSelected(
//                        convertMillisToDate(
//                            it,
//                            format = format
//                        )
//                    )
//                }
//            },
//            onDismiss = { showModal = false },
//            datePickerState = datePickerState
//        )
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DatePickerModalInput(
//    onDateSelected: (Long?) -> Unit,
//    onDismiss: () -> Unit,
//    datePickerState: DatePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
//) {
//
//    DatePickerDialog(
//        colors = DatePickerDefaults.colors(
//            containerColor = DarwinTouchNeutral50,
//            titleContentColor = DarwinTouchNeutral1000,
//            headlineContentColor = DarwinTouchNeutral1000,
//            weekdayContentColor = DarwinTouchNeutral1000,
//            subheadContentColor = DarwinTouchNeutral1000,
//            navigationContentColor = DarwinTouchNeutral1000,
//            yearContentColor = DarwinTouchNeutral800,
//            disabledYearContentColor = DarwinTouchNeutral400,
//            currentYearContentColor = DarwinTouchPrimary,
//            selectedYearContentColor = DarwinTouchNeutral0,
//            disabledSelectedYearContentColor = DarwinTouchNeutral0,
//            selectedYearContainerColor = DarwinTouchPrimary,
//            disabledSelectedYearContainerColor = DarwinTouchNeutral400,
//            dayContentColor = DarwinTouchNeutral1000,
//            disabledDayContentColor = DarwinTouchNeutral400,
//            selectedDayContentColor = DarwinTouchNeutral0,
//            disabledSelectedDayContentColor = DarwinTouchNeutral0,
//            selectedDayContainerColor = DarwinTouchPrimary,
//            disabledSelectedDayContainerColor = DarwinTouchNeutral400,
//            todayContentColor = DarwinTouchPrimary,
//            todayDateBorderColor = DarwinTouchPrimary,
//            dayInSelectionRangeContentColor = DarwinTouchNeutral0,
//            dayInSelectionRangeContainerColor = DarwinTouchPrimary,
//            dividerColor = DarwinTouchNeutral200,
//        ),
//        onDismissRequest = onDismiss,
//        confirmButton = {
//            ButtonWrapper(onClick = {
//                onDateSelected(datePickerState.selectedDateMillis)
//                onDismiss()
//            }, type = ButtonWrapperType.TEXT, text = "OK")
//        },
//        dismissButton = {
//            ButtonWrapper(onClick = onDismiss, type = ButtonWrapperType.TEXT, text = "Cancel")
//        }
//    ) {
//        DatePicker(
//            state = datePickerState, colors = DatePickerDefaults.colors(
//                containerColor = DarwinTouchNeutral50,
//                titleContentColor = DarwinTouchNeutral1000,
//                headlineContentColor = DarwinTouchNeutral1000,
//                weekdayContentColor = DarwinTouchNeutral1000,
//                subheadContentColor = DarwinTouchNeutral1000,
//                navigationContentColor = DarwinTouchNeutral1000,
//                yearContentColor = DarwinTouchNeutral800,
//                disabledYearContentColor = DarwinTouchNeutral400,
//                currentYearContentColor = DarwinTouchPrimary,
//                selectedYearContentColor = DarwinTouchNeutral0,
//                disabledSelectedYearContentColor = DarwinTouchNeutral0,
//                selectedYearContainerColor = DarwinTouchPrimary,
//                disabledSelectedYearContainerColor = DarwinTouchNeutral400,
//                dayContentColor = DarwinTouchNeutral1000,
//                disabledDayContentColor = DarwinTouchNeutral400,
//                selectedDayContentColor = DarwinTouchNeutral0,
//                disabledSelectedDayContentColor = DarwinTouchNeutral0,
//                selectedDayContainerColor = DarwinTouchPrimary,
//                disabledSelectedDayContainerColor = DarwinTouchNeutral400,
//                todayContentColor = DarwinTouchPrimary,
//                todayDateBorderColor = DarwinTouchPrimary,
//                dayInSelectionRangeContentColor = DarwinTouchNeutral0,
//                dayInSelectionRangeContainerColor = DarwinTouchPrimary,
//                dividerColor = DarwinTouchNeutral200,
//            )
//        )
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NativeDatePickerField(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    format: String = "EEE, dd MMM, yyyy",
    disableFutureDates: Boolean = false
) {
    val showDialog = remember { mutableStateOf(false) }

    val dateFormatter = remember(format) {
        DateTimeFormatter.ofPattern(format)
    }

    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = if (disableFutureDates) {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= System.currentTimeMillis()
                }
            }
        } else {
            DatePickerDefaults.AllDates
        }
    )

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog.value = true },
        label = { Text("Date") },
        readOnly = true,
        enabled = true,
        placeholder = { Text("EEE, dd MMM, yyyy") }
    )

    if (showDialog.value) {
        DatePickerDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val localDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onDateSelected(localDate.format(dateFormatter))
                        }
                        showDialog.value = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


fun convertMillisToDate(millis: Long, format: String = "dd/MM/yyyy"): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(Date(millis))
}