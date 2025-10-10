package eka.care.documents.ui.components.bottomSheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.components.common.BottomSheetContentLayout
import eka.care.documents.ui.state.TagsState
import eka.care.documents.ui.viewmodel.RecordsViewModel

@Composable
fun TagsBottomSheet(
    viewModel: RecordsViewModel,
    onTagsUpdated: (List<String>) -> Unit
) {
    val tagState by viewModel.tagsState.collectAsState()
    val preselectedTags = viewModel.tags.value.toMutableSet()

    BottomSheetContentLayout(
        height = 0.25f,
        title = "Filter by tag"
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            (tagState as? TagsState.Success)?.data?.distinctBy { it.tag }?.forEach {
                val isSelected = preselectedTags.contains(it.tag)
                InputChip(
                    selected = isSelected,
                    onClick = {
                        if(isSelected) {
                            preselectedTags.remove(it.tag)
                        } else {
                            preselectedTags.add(it.tag)
                        }
                        onTagsUpdated(preselectedTags.toList())
                    },
                    label = {
                        Text(
                            text = it.tag,
                            style = EkaTheme.typography.labelLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    border = BorderStroke(1.dp, EkaTheme.colors.outline)
                )
            }
        }
    }
}