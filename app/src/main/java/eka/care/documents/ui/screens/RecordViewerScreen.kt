package eka.care.documents.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.eka.ui.theme.EkaTheme
import eka.care.documents.ui.R
import eka.care.documents.ui.components.smartreport.ErrorState
import eka.care.documents.ui.components.smartreport.LoadingState
import eka.care.documents.ui.components.smartreport.RecordSuccessState
import eka.care.documents.ui.state.DocumentPreviewState
import eka.care.documents.ui.utility.FileSharing
import eka.care.documents.ui.viewmodel.RecordPreviewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordViewerScreen(
    viewModel: RecordPreviewViewModel,
    id: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(id) { viewModel.getDocument(id = id,) }
    val state by viewModel.document.collectAsState()
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EkaTheme.colors.surface
                ),
                title = {
                    Text(
                        text = "Document",
                        style = EkaTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .padding(2.dp),
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            FileSharing.handleFileDownload(
                                context = context,
                                uri = selectedUri
                            )
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(R.drawable.ic_download),
                            contentDescription = "More",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
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
