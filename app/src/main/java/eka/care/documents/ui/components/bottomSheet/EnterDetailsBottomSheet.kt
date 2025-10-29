package eka.care.documents.ui.components.bottomSheet

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R
import eka.care.documents.ui.components.DatePickerWrapper
import eka.care.documents.ui.components.common.BottomSheetContentLayout
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.state.UpsertRecordState
import eka.care.documents.ui.utility.formatLocalDateToCustomFormat
import eka.care.documents.ui.utility.timestampToLong
import eka.care.documents.ui.viewmodel.RecordsViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterDetailsBottomSheet(
    viewModel: RecordsViewModel,
    onClick: () -> Unit,
    caseId: String? = null,
    fileList: ArrayList<File>,
    paramsModel: MedicalRecordsNavModel,
    editDocument: Boolean
) {
    val context = LocalContext.current
    var selectedChip by remember { mutableStateOf(viewModel.cardClickData.value?.documentType) }
    var loadingState by remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf("") }
    val state by viewModel.upsertRecordsState.collectAsState()

    val dateInMillis = Calendar.getInstance().timeInMillis
    val sdf = SimpleDateFormat("EEE, dd MMM, yyyy", Locale.getDefault())

    val date = if (editDocument) {
        if (selectedDate.value.length > 1) selectedDate.value else Date(dateInMillis).run {
            sdf.format(
                this
            )
        } ?: "Add Date"
    } else {
        if (selectedDate.value.length > 1) selectedDate.value else "Add Date"
    }

    LaunchedEffect(state) {
        when (state) {
            is UpsertRecordState.Loading -> {
                loadingState = true
            }

            is UpsertRecordState.Error -> {
                loadingState = false
                Toast.makeText(
                    context,
                    (state as? UpsertRecordState.Error)?.error,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is UpsertRecordState.Success -> {
                loadingState = false
                onClick()
            }

            else -> {}
        }
    }

    var isAbhaEnabled by remember { mutableStateOf(paramsModel.isAbhaEnabled) }

    val onAddMedicalRecord = {
        if (editDocument) {
            viewModel.updateRecord(
                localId = viewModel.cardClickData.value?.id ?: "",
                docType = selectedChip ?: "ot",
                docDate = timestampToLong(date) ?: 0L
            )
            onClick()
        } else {
            if (selectedChip != null && fileList.isNotEmpty()) {
                viewModel.createRecord(
                    files = fileList,
                    businessId = paramsModel.businessId,
                    ownerId = paramsModel.ownerId,
                    caseId = caseId,
                    isAbhaLinked = isAbhaEnabled,
                    documentType = selectedChip ?: "ot",
                    documentDate = timestampToLong(date)
                )
            }
        }
    }

    val datePickerStateRecord = rememberDatePickerState(
        initialSelectedDateMillis = LocalDate.now().toEpochDay() * 24 * 60 * 60 * 1000,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }
        }
    )

    LaunchedEffect(datePickerStateRecord.selectedDateMillis) {
        datePickerStateRecord.selectedDateMillis?.let { selectedMillis ->
            val selectedDateObj = Date(selectedMillis)
            selectedDate.value = formatLocalDateToCustomFormat(selectedDateObj) ?: ""
        }
    }

    BottomSheetContentLayout(
        title = if (editDocument) "Edit Medical Record" else "Add Record Details",
        height = .50f,
        bottomStickyContent = {
            Button(
                onClick = onAddMedicalRecord,
                enabled = selectedChip != null && !loadingState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp)
            ) {
                if (loadingState) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier
                            .size(20.dp)
                    )
                } else {
                    Text(text = "Save")
                }
            }
        }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_file_regular),
                    contentDescription = "File",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(paramsModel.documentTypes) { recordInfo ->
                        InputChip(
                            selected = selectedChip == recordInfo.id,
                            onClick = { selectedChip = recordInfo.id },
                            label = {
                                Text(text = recordInfo.name)
                            }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar_regular),
                    contentDescription = "File",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                DatePickerWrapper(
                    selectedDate = date,
                    label = "",
                    datePickerState = datePickerStateRecord,
                    format = "EEE, dd MMM, yyyy",
                    onDateSelected = {
                        selectedDate.value = it
                    }
                )
            }
            if(paramsModel.isAbhaEnabled) {
                Row(
                    Modifier.fillMaxWidth()
                        .toggleable(
                            value = isAbhaEnabled,
                            onValueChange = {
                                isAbhaEnabled = it
                            },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isAbhaEnabled,
                        onCheckedChange = null
                    )
                    Text(
                        text = "Link record with my ABHA account",
                        style = EkaTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}