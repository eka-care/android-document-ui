package eka.care.documents.ui.components.girdView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import eka.care.doctor.theme.color.DarwinTouchNeutral0
import eka.care.doctor.theme.color.DarwinTouchNeutral200
import eka.care.doctor.theme.color.DarwinTouchNeutral50

@Composable
fun RecordsGridItemShimmer() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = DarwinTouchNeutral0
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(184.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                            .shimmer()
                            .background(
                                color = DarwinTouchNeutral200,
                                RoundedCornerShape(4.dp)
                            )
                    ) { }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(
                            modifier = Modifier
                                .width(96.dp)
                                .height(16.dp)
                                .shimmer()
                                .background(
                                    color = DarwinTouchNeutral200,
                                    RoundedCornerShape(28.dp)
                                )
                        ) { }
                        Box(
                            modifier = Modifier
                                .width(66.dp)
                                .height(16.dp)
                                .shimmer()
                                .background(
                                    color = DarwinTouchNeutral200,
                                    RoundedCornerShape(28.dp)
                                )
                        ) { }
                    }
                }
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                        .shimmer()
                        .background(
                            color = DarwinTouchNeutral200,
                            RoundedCornerShape(28.dp)
                        )
                ) { }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .shimmer()
                    .background(
                        color = DarwinTouchNeutral200,
                        RoundedCornerShape(12.dp)
                    )
            ) { }
        }
    }
}

@Composable
fun RecordsGridShimmer() {
    LazyVerticalGrid(
        modifier = Modifier.background(DarwinTouchNeutral50),
        contentPadding = PaddingValues(12.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(6) {
            item {
                RecordsGridItemShimmer()
            }
        }
    }
}

@Preview
@Composable
fun RecordsListItemShimmerPreview() {
    RecordsGridShimmer()
}