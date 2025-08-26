package eka.care.documents.ui.components.bottomSheet

import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R
import eka.care.documents.ui.components.common.BottomSheetContentLayout
import eka.care.documents.ui.state.CasesState
import eka.care.documents.ui.viewmodel.RecordsViewModel
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun CaseDetailsBottomSheet(
    viewModel: RecordsViewModel,
    caseId: String,
) {
    LaunchedEffect(Unit) {
        viewModel.getCaseDetails(caseId)
    }
    val state = viewModel.getCaseDetailsState.collectAsState().value
    when(state) {
        is CasesState.Success -> {
            BottomSheetContentLayout(
                height = 0.1f * 4,
                title = "Case details",
            ) {
                CaseDetailItem(
                    title = "Case ID",
                    subtitle = state.data.firstOrNull()?.id ?: "",
                    icon = R.drawable.ic_case_id
                )
                CaseDetailItem(
                    title = "Case create on",
                    subtitle = formatTimestamp(state.data.firstOrNull()?.createdAt ?: 0L),
                    icon = R.drawable.ic_calendar_regular
                )
                CaseDetailItem(
                    title = "Case updated on",
                    subtitle = formatTimestamp(state.data.firstOrNull()?.updatedAt ?: 0L),
                    icon = R.drawable.ic_calendar_regular
                )
            }
        }
        is CasesState.Loading -> {
            BottomSheetContentLayout(
                height = 0.1f * 4,
                title = "Case details"
            ) {

            }
        }
        else -> {}
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CaseDetailItem(
    title: String,
    subtitle: String,
    icon: Int
) {
    androidx.compose.material3.ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.White
        ),
        headlineContent = {
            Text(
                text = title,
                style = EkaTheme.typography.labelMedium,
                color = EkaTheme.colors.onSurfaceVariant
            )
        },
        supportingContent = {
            Text(
                text = subtitle,
                style = EkaTheme.typography.bodyLarge,
                color = EkaTheme.colors.onSurface
            )
        },
        leadingContent = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = icon),
                contentDescription = "Prescription",
                tint = EkaTheme.colors.onSurface
            )
        }
    )
}

private fun formatTimestamp(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
        .withZone(ZoneOffset.UTC) // or ZoneOffset.UTC if you want UTC
    return formatter.format(Instant.ofEpochMilli(timestamp * 1000))
}