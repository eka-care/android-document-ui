package eka.care.documents.ui.components.recordgridview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R
import eka.care.documents.ui.components.SmartTag
import eka.care.documents.ui.utility.DocumentUtility.Companion.getDocumentDate
import eka.care.documents.ui.utility.DocumentUtility.Companion.getTitleById
import eka.care.documents.ui.utility.GetIconById
import eka.care.documents.ui.utility.Mode
import eka.care.documents.ui.utility.RecordType
import eka.care.documents.ui.utility.TagState
import eka.care.records.client.model.RecordModel
import eka.care.records.client.model.RecordUiState

@Composable
fun RecordsGridItem(
    record: RecordModel,
    mode: Mode,
    isSelected: Boolean,
    tagState: TagState? = null,
    onClick: () -> Unit,
    onRetry: () -> Unit,
    onMoreOptionsClick: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(EkaTheme.colors.surface),
        onClick = if (record.uiState == RecordUiState.SYNC_FAILED) onRetry else onClick
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.padding(3.dp)) {
                        RecordType.entries.find { it.code == record.documentType }?.let {
                            GetIconById(
                                type = it,
                                padding = 3.dp,
                                iconSize = 12.dp,
                                boundingBoxSize = 18.dp,
                                roundedCorner = 4.dp,
                            )
                        }
                    }
                    Column {
                        Text(
                            modifier = Modifier.padding(end = 12.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = getTitleById(record.documentType),
                            style = EkaTheme.typography.titleSmall,
                            color = EkaTheme.colors.onSurface
                        )
                        record.documentDate?.let {
                            Text(
                                text = getDocumentDate(it),
                                overflow = TextOverflow.Ellipsis,
                                style = EkaTheme.typography.labelSmall,
                                color = EkaTheme.colors.outline
                            )
                        }
                    }
                }
                if (isSelected && mode == Mode.SELECTION) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = CircleShape
                            )
                            .padding(2.dp)
                    )
                } else {
                    IconButton(
                        onClick = {
                            onMoreOptionsClick.invoke()
                        },
                        content = {
                            Icon(
                                modifier = Modifier.size(24.dp).padding(4.dp),
                                painter = painterResource(id =R.drawable.ic_ellipsis_vertical_regular),
                                contentDescription = "More",
                                tint = EkaTheme.colors.onSurface
                            )
                        }
                    )
                }
            }
            val blurModifier =
                when (record.uiState) {
                    RecordUiState.SYNCING -> Modifier.blur(6.dp)
                    RecordUiState.SYNC_FAILED -> Modifier.blur(22.dp)
                    RecordUiState.WAITING_TO_UPLOAD, RecordUiState.WAITING_FOR_NETWORK -> Modifier.blur(
                        6.dp
                    )

                    else -> Modifier
                }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(record.thumbnail)
                        .crossfade(700)
                        .build(),
                    contentScale = ContentScale.FillWidth,
                    alignment = Alignment.TopCenter,
                    fallback = painterResource(R.drawable.ic_record_placeholder_custom),
                    contentDescription = record.updatedAt.toString(),
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(12.dp))
                        .then(blurModifier)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            if (record.uiState !in listOf(RecordUiState.NONE, RecordUiState.SYNC_SUCCESS)) {
                                Color.White.copy(alpha = 0.9f)
                            } else {
                                Color.Black.copy(alpha = 0.2f)
                            },
                            RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp))
                )

                when (record.uiState) {
                    RecordUiState.SYNCING -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(18.dp)
                                    .align(Alignment.CenterHorizontally),
                                color = EkaTheme.colors.onSurface,
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Uploading",
                                style = EkaTheme.typography.labelSmall,
                                color = EkaTheme.colors.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    RecordUiState.WAITING_TO_UPLOAD -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_cloud_slash_regular),
                                contentDescription = "Upload",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 8.dp).size(24.dp),
                                tint = EkaTheme.colors.onSurface
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Waiting to upload",
                                style = EkaTheme.typography.labelSmall,
                                color = EkaTheme.colors.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    RecordUiState.WAITING_FOR_NETWORK -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_cloud_slash_regular),
                                contentDescription = "Upload",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 8.dp).size(24.dp),
                                tint = EkaTheme.colors.onSurface
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Waiting for network",
                                style = EkaTheme.typography.labelSmall,
                                color = EkaTheme.colors.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    RecordUiState.SYNC_FAILED -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrows_rotate_regular),
                                contentDescription = "Upload",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 8.dp).size(24.dp),
                                tint = EkaTheme.colors.onSurface
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Try again",
                                style = EkaTheme.typography.labelSmall,
                                color = EkaTheme.colors.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    else -> {

                    }
                }

                if (record.isSmart) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .align(Alignment.BottomEnd)
                    ) {
                        SmartTag(modifier = Modifier.align(Alignment.BottomEnd))
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun MedicalRecordsGridItemPreview() {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(RecordType.entries) {
            RecordsGridItem(
                tagState = TagState.GENERATING,
                mode = Mode.SELECTION,
                onClick = {},
                record = RecordModel(
                    id = "hhh",
                    thumbnail = null,
                    uiState = RecordUiState.SYNC_FAILED,
                    createdAt = 0L,
                    updatedAt = 0L,
                    documentDate = 0L,
                    documentType = "",
                    isSmart = false,
                    smartReport = "",
                    files = emptyList(),
                ),
                onMoreOptionsClick = {},
                onRetry = {},
                isSelected = true
            )
        }
    }
}