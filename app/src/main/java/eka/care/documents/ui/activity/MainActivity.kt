package eka.care.documents.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.screens.RecordsMainScreen
import eka.care.documents.ui.theme.RecordsColorScheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EkaTheme(colorScheme = RecordsColorScheme) {
                RecordsMainScreen()
            }
        }
    }

}