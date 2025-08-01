package eka.care.records.ui.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eka.care.doctor.icons.R
import eka.care.doctor.theme.color.DarwinTouchNeutral0
import eka.care.doctor.typography.touchBodyRegular
import eka.care.doctor.typography.touchTitle3Bold
import eka.care.doctor.ui.molecule.ButtonWrapper

@Composable
fun RecordEmptyState(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarwinTouchNeutral0),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_no_medical_records_custom),
            contentDescription = "no_files",
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Text(
            text = "No Medical Records Yet",
            color = MaterialTheme.colorScheme.onSurface,
            style = touchTitle3Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Add or upload files to get started",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = touchBodyRegular,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        ButtonWrapper(
            text = "Add Files",
            onClick = onClick
        )
    }
}
