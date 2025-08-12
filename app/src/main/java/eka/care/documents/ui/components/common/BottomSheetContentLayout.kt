package eka.care.documents.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetContentLayout(
    modifier: Modifier = Modifier,
    title: String? = "",
    height: Float = 0.8f,
    topBar: (@Composable () -> Unit)? = null,
    bottomStickyContent: @Composable () -> Unit = { },
    content: @Composable () -> Unit,
) {
    val screenHeight: Dp = LocalConfiguration.current.screenHeightDp.dp

    Scaffold(
        modifier = modifier
            .fillMaxWidth()
            .height(screenHeight.times(height)),
        topBar = {
            if (topBar != null)
                topBar()
            else {
                SheetTopBar()
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                if (!title.isNullOrEmpty()) {
                    item {
                        Text(
                            text = title,
                            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                        )
                    }
                }
                item {
                    content()
                }
            }
        },
        bottomBar = bottomStickyContent
    )
}


@Composable
fun SheetTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .width(32.dp)
                .height(4.dp)
        )
    }
}

@Composable
@Preview
private fun Preview() {
    BottomSheetContentLayout(
        title = "Title",
        bottomStickyContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp)
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}