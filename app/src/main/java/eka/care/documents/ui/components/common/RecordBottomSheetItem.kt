package eka.care.documents.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eka.care.doctor.records.R
import eka.care.doctor.theme.color.DarwinTouchNeutral0
import eka.care.doctor.typography.touchBodyRegular
import eka.care.doctor.ui.atom.IconWrapper
import eka.care.documents.ui.model.DocumentBottomSheetItemModel
import eka.care.records.ui.presentation.model.DocumentBottomSheetItemModel

@Composable
fun RecordBottomSheetItem(
    item: DocumentBottomSheetItemModel,
    onClick: () -> Unit,
) {
    ListItem(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(
                bounded = true
            ),
            onClick = onClick
        ),
        headlineContent = {
            Text(
                text = item.itemName,
                style = touchBodyRegular,
                color = item.itemNameColor
            )
        },
        leadingContent = {
            IconWrapper(
                icon = item.leadingIcon,
                contentDescription = "",
                tint = item.leadingIconTint,
                modifier = Modifier.size(16.dp)
            )
        },
        trailingContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.isRecommended) {
                    RecommendedChip(text = stringResource(id = R.string.recommended))
                }
                IconWrapper(
                    icon = item.trailingIcon,
                    modifier = Modifier.size(16.dp),
                    contentDescription = "action"
                )
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = DarwinTouchNeutral0
        )
    )
}

@Composable
fun RecommendedChip(text: String) {
    Box(
        modifier = Modifier
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}