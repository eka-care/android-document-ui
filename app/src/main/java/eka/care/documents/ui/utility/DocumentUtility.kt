package eka.care.documents.ui.utility

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
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
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
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

        fun loadFromUri(context: Context, photoUri: Uri): Bitmap? {
            var bitmap: Bitmap? = null
            try {
                bitmap = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                    val source: ImageDecoder.Source =
                        ImageDecoder.createSource(context.contentResolver, photoUri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, photoUri)
                }
            } catch (ex: IOException) {
                Log.e("log", "Exception in loadFromUri() = ", ex)
            }
            return bitmap
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


enum class DocumentViewType {
    ListView, GridView
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

fun timestampToLong(timestamp: String, format: String = "EEE, dd MMM, yyyy"): Long? {
    if (timestamp == "Add Date") {
        return null
    }
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    val date =
        dateFormat.parse(timestamp) ?: throw IllegalArgumentException("Invalid date format")
    return date.time / 1000
}

fun formatLocalDateToCustomFormat(date: Date): String? {
    val formatter = SimpleDateFormat("EEE, dd MMM, yyyy", Locale.getDefault())
    return formatter.format(date)
}

enum class DocumentBottomSheetType {
    DocumentUpload, DocumentOptions, DocumentSort, EnterFileDetails
}

enum class CaseType(
    val id: String,
    val displayName: String,
    val iconRes: Int
) {
    OP_CONSULTATION("OPConsultation", "OP Consultation", R.drawable.ic_user_doctor),
    PRESCRIPTION("Prescription", "Prescription", R.drawable.ic_bed),
    DISCHARGE_SUMMARY("DischargeSummary", "Discharge Summary", R.drawable.ic_stethoscope),
    DIAGNOSTIC_REPORT("DiagnosticReport", "Diagnostic Report", R.drawable.ic_house),
    IMMUNIZATION_RECORD("ImmunizationRecord", "Immunization Record", R.drawable.ic_video),
    HEALTH_DOCUMENT_RECORD("HealthDocumentRecord", "Health Document Record", R.drawable.ic_ambulance),
    WELLNESS_RECORD("WellnessRecord", "Wellness Record", R.drawable.ic_scalpel);

    companion object {
        fun fromId(id: String): CaseType? {
            return entries.find { it.id == id }
        }
    }
}
