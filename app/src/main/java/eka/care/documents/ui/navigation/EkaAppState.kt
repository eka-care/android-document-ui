package eka.care.documents.ui.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.stefanoq21.material3.navigation.BottomSheetNavigator
import kotlinx.coroutines.CoroutineScope

@Stable
class EkaAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val bottomSheetNavigator: BottomSheetNavigator
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)
    private val _showAlertDialog = mutableStateOf(false)
}