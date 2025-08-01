package eka.care.documents.ui.components.recordListView

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R
import eka.care.documents.ui.components.SmartTag
import eka.care.documents.ui.utility.DocumentUtility.Companion.getTitleById
import eka.care.documents.ui.utility.GetIconById
import eka.care.documents.ui.utility.RecordType
import eka.care.documents.ui.utility.TagState
import eka.care.records.client.model.RecordModel
import eka.care.records.client.model.RecordStatus


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
            onClick = onClick,
        ),
        colors = ListItemDefaults.colors(
            containerColor = EkaTheme.colors.background
        ),
        headlineContent = {
            Text(
                text = getTitleById(record.documentType),
                style = EkaTheme.typography.bodyLarge,
                color = EkaTheme.colors.onSurface
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
                            style = EkaTheme.typography.labelLarge,
                            color = EkaTheme.colors.outline
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

                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = {
                        onMoreOptionsClick
                    },
                    content = {
                        Icon(
                            painter = painterResource(id =R.drawable.ic_ellipsis_vertical_regular),
                            contentDescription = "More",
                            tint = EkaTheme.colors.onSurface
                        )
                    }
                )
            }
        },
    )
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

