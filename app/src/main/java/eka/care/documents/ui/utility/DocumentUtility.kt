package eka.care.documents.ui.utility

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R
import java.text.SimpleDateFormat
import java.util.Locale

class DocumentUtility {
    companion object {
        fun getDocumentDate(documentDate: Long): String {
            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return formatter.format(documentDate * 1000)
        }

        fun getTitleById(id: String): String {
            return RecordType.entries.find { it.code == id }?.title ?: ""
        }
    }
}

enum class Mode {
    VIEW, SELECTION;

    companion object {
        fun valueOf(value: Int): Mode {
            return if (value == SELECTION.ordinal) {
                SELECTION
            } else {
                VIEW
            }
        }
    }
}


enum class TagState {
    GENERATING,
    SMART_REPORT,
}

enum class RecordType(val code: String, val title: String) {
    LAB_REPORT("lr", "Lab Report"),
    PRESCRIPTION("ps", "Prescription"),
    INSURANCE("in", "Insurance"),
    SCAN("sc", "Scan"),
    DISCHARGE_SUMMARY("ds", "Discharge Summary"),
    VACCINE_CERTIFICATE("vc", "Vaccine Certificate"),
    INVOICE("iv", "Invoice"),
    OTHERS("ot", "Other")
}

@Composable
fun GetIconById(
    type: RecordType,
    padding: Dp = 6.dp,
    iconSize: Dp = 12.dp,
    roundedCorner: Dp = 4.dp,
    boundingBoxSize: Dp = 12.dp
) {
    when (type) {
        RecordType.LAB_REPORT -> {
            Box(
                modifier = Modifier.background(Color(0xFF19A66A), RoundedCornerShape(roundedCorner))
                    .padding(padding)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_vial_regular),
                    contentDescription = "Lab Report",
                    modifier = Modifier.size(iconSize),
                    tint = EkaTheme.colors.onPrimary
                )
            }
        }

        RecordType.PRESCRIPTION -> {
            Box(
                modifier = Modifier.background(EkaTheme.colors.error, RoundedCornerShape(roundedCorner))
                    .padding(padding)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pills_regular),
                    contentDescription = "Prescription",
                    modifier = Modifier.size(iconSize),
                    tint = EkaTheme.colors.onPrimary
                )
            }
        }

        RecordType.INSURANCE -> {
            Box(
                modifier = Modifier.background(
                    EkaTheme.colors.primary, RoundedCornerShape(roundedCorner)
                )
                    .padding(padding)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_clipboard_medical_regular),
                    contentDescription = "Insurance",
                    modifier = Modifier.size(iconSize),
                    tint = EkaTheme.colors.onPrimary
                )
            }
        }

        RecordType.SCAN -> {
            Box(
                modifier = Modifier.background(EkaTheme.colors.error, RoundedCornerShape(roundedCorner))
                    .padding(padding)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera_viewfinder_regular),
                    contentDescription = "Scan",
                    modifier = Modifier.size(iconSize),
                    tint = EkaTheme.colors.onPrimary
                )
            }
        }

        RecordType.DISCHARGE_SUMMARY -> {
            Box(
                modifier = Modifier.background(Color(0xFF19A66A), RoundedCornerShape(roundedCorner))
                    .padding(padding)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_file_check_regular),
                    contentDescription = "Discharge Summary",
                    modifier = Modifier.size(iconSize),
                    tint = EkaTheme.colors.onPrimary
                )
            }
        }

        RecordType.VACCINE_CERTIFICATE -> {
            Box(
                modifier = Modifier.background(
                    EkaTheme.colors.primary, RoundedCornerShape(roundedCorner)
                )
                    .padding(padding)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_shield_virus_regular),
                    contentDescription = "Vaccine Certificate",
                    modifier = Modifier.size(iconSize),
                    tint = EkaTheme.colors.onPrimary
                )
            }
        }

        RecordType.INVOICE -> {
            Box(
                modifier = Modifier.background(EkaTheme.colors.error, RoundedCornerShape(roundedCorner))
                    .padding(padding)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_receipt_regular),
                    contentDescription = "Invoice",
                    modifier = Modifier.size(iconSize),
                    tint = EkaTheme.colors.onPrimary
                )

            }
        }

        RecordType.OTHERS -> {
            Box(
                modifier = Modifier.background(Color(0xFF19A66A), RoundedCornerShape(roundedCorner))
                    .padding(padding)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_file_medical_regular),
                    contentDescription = "Other",
                    modifier = Modifier.size(iconSize),
                    tint = EkaTheme.colors.onPrimary
                )
            }
        }
    }
}

enum class CaseType(
    val displayName: String,
    val iconRes: Int
) {
    DOCTOR_VISIT("Doctor Visit (OPD)", R.drawable.ic_user_doctor),
    HOSPITAL_ADMIT("Hospital Admit (IPD)", R.drawable.ic_bed),
    HEALTH_CHECKUP("Health Check-up", R.drawable.ic_stethoscope),
    HOME_VISIT("Home Visit", R.drawable.ic_house),
    TELECONSULTATION("Teleconsultation", R.drawable.ic_video),
    EMERGENCY("Emergency", R.drawable.ic_ambulance),
    SURGERY("Surgery / Procedure", R.drawable.ic_scalpel),
    DENTAL("Dental", R.drawable.ic_tooth),
    SECOND_OPINION("Second Opinion", R.drawable.ic_users),
    OTHER("Other", R.drawable.ic_folder),
    CREATE_OWN("Create own", R.drawable.ic_folder)
}
