package eka.care.documents.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.theme.AppColorScheme
import eka.care.documents.ui.viewmodel.RecordsViewModel

class MainActivity : ComponentActivity() {
    private val viewModel = ViewModelProvider(this).get(RecordsViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EkaTheme(colorScheme = AppColorScheme) {
//                RecordsMainScreen(viewModel)
            }
        }
    }

}