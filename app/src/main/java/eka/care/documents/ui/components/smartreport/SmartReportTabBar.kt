package eka.care.documents.ui.components.smartreport

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme


@Composable
fun SmartReportTabBar(
    pagerState: PagerState,
    onTabSelect: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        val tabData = listOf(
            "Smart Report",
            "Original Record"
        )
        CustomScrollableTabRow(
            pagerState = pagerState,
            tabs = tabData,
            onTabClick = onTabSelect
        )
    }
}

@Composable
fun CustomScrollableTabRow(
    pagerState: PagerState,
    tabs: List<String>,
    onTabClick: (Int) -> Unit
) {
    val density = LocalDensity.current
    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(tabs.size) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }
    val tabWidth = (LocalConfiguration.current.screenWidthDp.dp / tabs.size)
    ScrollableTabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = pagerState.currentPage,
        containerColor = EkaTheme.colors.surface,
        edgePadding = 0.dp,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.customTabIndicatorOffset(
                    currentTabPosition = tabPositions[pagerState.currentPage],
                    tabWidth = tabWidths[pagerState.currentPage]
                )
            )
        }
    ) {
        tabs.forEachIndexed { tabIndex, tab ->
            Tab(
                modifier = Modifier.width(tabWidth),
                selected = pagerState.currentPage == tabIndex,
                onClick = { onTabClick(tabIndex) },
                text = {
                    Text(
                        text = tab,
                        onTextLayout = { textLayoutResult ->
                            tabWidths[tabIndex] =
                                with(density) { textLayoutResult.size.width.toDp() }
                        }
                    )
                }
            )
        }
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition,
    tabWidth: Dp
): Modifier = composed {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = ""
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth) / 2),
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = ""
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}