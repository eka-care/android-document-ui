package eka.care.records.ui.presentation.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import eka.care.doctor.icons.R
import eka.care.doctor.theme.color.DarwinTouchNeutral0
import eka.care.doctor.ui.molecule.IconButtonWrapper
import eka.care.doctor.ui.organism.AppBar
import eka.care.records.ui.presentation.components.ErrorState
import eka.care.records.ui.presentation.components.LoadingState
import eka.care.records.ui.presentation.components.RecordSuccessState
import eka.care.records.ui.presentation.components.handleFileDownload
import eka.care.documents.ui.navigation.RecordViewerNavModel
import eka.care.documents.ui.state.DocumentPreviewState
import eka.care.documents.ui.viewmodel.RecordPreviewViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordViewerScreen(navData: RecordViewerNavModel, onBackClick: () -> Unit) {
    val viewModel: RecordPreviewViewModel = koinViewModel()
    val context = LocalContext.current

    LaunchedEffect(navData) {
        viewModel.getDocument(
            id = navData.localId,
        )
    }
    val state by viewModel.document.collectAsState()
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    Scaffold(
        topBar = {
            AppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarwinTouchNeutral0),
                title = "Document",
                navigationIcon = {
                    IconButtonWrapper(
                        onClick = onBackClick,
                        icon = R.drawable.ic_arrow_left_regular,
                        contentDescription = "Back",
                    )
                },
                actions = {
                    IconButtonWrapper(
                        onClick = {
                            handleFileDownload(
                                context = context,
                                uri = selectedUri
                            )
                        },
                        icon = R.drawable.ic_download_regular,
                        contentDescription = "Close",
                    )
                },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (state) {
                    is DocumentPreviewState.Loading -> LoadingState()
                    is DocumentPreviewState.Error -> ErrorState((state as DocumentPreviewState.Error).message)
                    is DocumentPreviewState.Success -> {
                        RecordSuccessState(
                            record = state as DocumentPreviewState.Success,
                            onUriSelected = { uri -> selectedUri = uri }
                        )
                    }
                }
            }
        }
    )
}
