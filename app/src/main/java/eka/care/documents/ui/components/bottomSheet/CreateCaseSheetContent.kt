package eka.care.documents.ui.components.bottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eka.ui.buttons.EkaButton
import com.eka.ui.buttons.EkaButtonShape
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R
import eka.care.documents.ui.components.CreateCaseDateField
import eka.care.documents.ui.components.CreateCaseDatePicker
import eka.care.documents.ui.components.CreateCaseTypeDropdown
import eka.care.documents.ui.components.common.TextFieldWrapper
import eka.care.documents.ui.utility.CaseType

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
            text = "Use a Case to organize all documents related to a medical event, whether it's a doctor visit, " +
                    "hospital admission, illness, or surgery. For easy access in one place.",
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

@Preview()
@Composable
fun CreateCaseContentPreview() {
    MaterialTheme {
        CreateCaseSheetContent()
    }
}
