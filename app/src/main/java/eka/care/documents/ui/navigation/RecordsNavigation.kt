package eka.care.documents.ui.navigation

import androidx.activity.compose.LocalActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import com.stefanoq21.material3.navigation.ModalBottomSheetLayout
import kotlinx.serialization.Serializable

@Serializable
internal data object BaseRoute

internal fun NavController.openSheet(
    route: BottomSheetStates,
    navOptions: NavOptions? = null,
) {
    navigate(route = route, navOptions)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsNavHost(
    modifier: Modifier = Modifier,
    appState: EkaAppState
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val navController = appState.navController
    val activity = LocalActivity.current
    val context = LocalContext.current

    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxSize(),
        bottomSheetNavigator = appState.bottomSheetNavigator,
        containerColor = Color.White
    ) {
        NavHost(
            modifier = modifier,
            startDestination = BaseRoute,
            navController = navController,
        ) {

        }
    }
}