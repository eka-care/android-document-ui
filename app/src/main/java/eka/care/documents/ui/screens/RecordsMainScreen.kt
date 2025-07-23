package eka.care.documents.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.components.RecordsHeader
import eka.care.documents.ui.theme.RecordsColorScheme

@Composable
@Preview
fun RecordsMainScreen() {
    EkaTheme(
        colorScheme = RecordsColorScheme
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize().background(Color.White),
            topBar = {
                RecordsHeader(

                )
            },
            content = {
                it.calculateTopPadding()

            }
        )
    }
}