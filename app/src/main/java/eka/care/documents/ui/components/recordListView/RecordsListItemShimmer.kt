package eka.care.documents.ui.components.recordListView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import com.valentinilk.shimmer.shimmer

@Composable
fun RecordsListItemShimmer() {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor =  EkaTheme.colors.onPrimary
        ),
        headlineContent = {
            Box(
                modifier = Modifier
                    .width(178.dp)
                    .height(16.dp)
                    .shimmer()
                    .background(
                        color = EkaTheme.colors.onPrimaryContainer,
                        RoundedCornerShape(28.dp)
                    )
            ) { }
        },
        supportingContent = {
            Column {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(66.dp)
                        .height(16.dp)
                        .shimmer()
                        .background(
                            color = EkaTheme.colors.onPrimaryContainer,
                            RoundedCornerShape(28.dp)
                        )
                ) { }
            }
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .shimmer()
                    .background(
                        color = EkaTheme.colors.onPrimaryContainer,
                        RoundedCornerShape(8.dp)
                    )
            ) { }
        },
        trailingContent = {
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .shimmer()
                    .background(
                        color = EkaTheme.colors.onPrimaryContainer,
                        RoundedCornerShape(28.dp)
                    )
            ) { }
        },
    )
}

@Composable
fun RecordsListShimmer() {
    Column(
        modifier = Modifier.background(EkaTheme.colors.onPrimaryContainer)
    ) {
        repeat(5) {
            RecordsListItemShimmer()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecordsListItemShimmerPreview() {
    RecordsListShimmer()
}