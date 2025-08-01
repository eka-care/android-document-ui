package eka.care.documents.ui.components.listView

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eka.care.doctor.icons.R
import eka.care.doctor.theme.color.DarwinTouchNeutral0
import eka.care.doctor.theme.color.DarwinTouchNeutral1000
import eka.care.doctor.theme.color.DarwinTouchNeutral200
import eka.care.doctor.theme.color.DarwinTouchNeutral600
import eka.care.doctor.typography.touchBodyRegular
import eka.care.doctor.typography.touchCalloutRegular
import eka.care.doctor.typography.touchLabelBold
import eka.care.doctor.ui.atom.IconWrapper
import eka.care.doctor.ui.molecule.IconButtonWrapper
import eka.care.records.client.model.RecordModel
import eka.care.records.client.model.RecordStatus
import eka.care.records.ui.utility.DocumentUtility.Companion.getTitleById
import eka.care.records.ui.utility.GetIconById
import eka.care.records.ui.utility.RecordType

enum class TagState {
    GENERATING,
    SMART_REPORT,
}

@Composable
fun RecordsListItem(
    record: RecordModel,
    subtitle: String? = null,
    tagState: TagState? = null,
    onClick: () -> Unit,
    onMoreOptionsClick: () -> Unit,
) {
    ListItem(
        modifier = Modifier.clickable(
            onClick = onClick
        ),
        colors = ListItemDefaults.colors(
            containerColor = DarwinTouchNeutral0
        ),
        headlineContent = {
            Text(
                text = getTitleById(record.documentType),
                style = touchBodyRegular,
                color = DarwinTouchNeutral1000
            )
        },
        supportingContent = {
            when (record.status) {
                RecordStatus.SYNCING -> {
                    Row {
                        Text(text = "Uploading")
                    }
                }

                RecordStatus.WAITING_TO_UPLOAD -> {
                    Row {
                        Text(text = "Waiting to upload")
                    }
                }

                RecordStatus.WAITING_FOR_NETWORK -> {
                    Row {
                        Text(text = "Waiting for network")
                    }
                }

                else -> {
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = touchCalloutRegular,
                            color = DarwinTouchNeutral600
                        )
                    }
                }
            }

        },
        leadingContent = {
            RecordType.entries.find { it.code == record.documentType }?.let { GetIconById(it) }
        },
        trailingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (record.isSmart) {
                    SmartTag()
                }

                IconButtonWrapper(
                    icon = R.drawable.ic_ellipsis_vertical_regular,
                    contentDescription = "More",
                    iconSize = 16.dp,
                    onClick = onMoreOptionsClick
                )
            }
        },
    )
}

@Composable
fun SmartTag(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .border(1.dp, DarwinTouchNeutral200, RoundedCornerShape(8.dp))
            .background(DarwinTouchNeutral0, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        IconWrapper(
            icon = R.drawable.ic_sparkle_custom,
            tint = Color.Unspecified,
            modifier = Modifier.size(16.dp),
            boundingBoxSize = 16.dp,
            contentDescription = "Custom Sparkle"
        )
        Text(text = "Smart", style = touchLabelBold, color = DarwinTouchNeutral1000)
    }
}


@Preview(showBackground = true)
@Composable
fun RecordsListItemPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        RecordType.entries.map {
            RecordsListItem(
                tagState = TagState.SMART_REPORT,
                subtitle = "17th Oct",
                onClick = {},
                record = RecordModel(
                    id = "hhh",
                    thumbnail = null,
                    status = RecordStatus.SYNC_SUCCESS,
                    createdAt = 0L,
                    updatedAt = 0L,
                    documentDate = 0L,
                    documentType = "",
                    isSmart = true,
                    smartReport = "",
                    files = emptyList(),
                ),
                onMoreOptionsClick = {}
            )
        }
    }
}

