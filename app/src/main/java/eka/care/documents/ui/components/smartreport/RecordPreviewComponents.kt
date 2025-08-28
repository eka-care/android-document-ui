package eka.care.documents.ui.components.smartreport

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.eka.ui.theme.EkaTheme
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState
import eka.care.documents.ui.state.DocumentPreviewState
import java.io.File

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message)
    }
}


@Composable
fun ImagePreview(uri: Uri, modifier: Modifier) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    AsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    // Update the scale based on zoom gestures.
                    scale *= zoom

                    // Limit the zoom levels within a certain range (optional).
                    scale = scale.coerceIn(0.5f, 3f)

                    // Update the offset to implement panning when zoomed.
                    offset = if (scale == 1f) Offset(0f, 0f) else offset + pan
                }
            }
            .graphicsLayer(
                scaleX = scale, scaleY = scale,
                translationX = offset.x, translationY = offset.y
            ),
        model = ImageRequest.Builder(LocalContext.current).data(uri).build(),
        contentScale = ContentScale.Fit,
        contentDescription = ""
    )
}

@Composable
fun RecordSuccessState(
    record: DocumentPreviewState.Success?,
    onUriSelected: (Uri) -> Unit
) {
    val context = LocalContext.current
    val filePaths = record?.data?.files?.mapNotNull { it.filePath?.toUri() } ?: emptyList()
    val file = File(filePaths.firstOrNull()?.path ?: "")
    when (file.extension) {
        "pdf" -> {
            record?.data?.files?.firstOrNull()?.filePath?.let { pdfUriString ->
                val uri: Uri = FileProvider.getUriForFile(
                    context,
                    "eka.care.doctor.fileprovider.new",
                    File(file.absolutePath)
                )

                onUriSelected(uri)

                val pdfVerticalReaderState = rememberVerticalPdfReaderState(
                    resource = ResourceType.Local(uri),
                    isZoomEnable = true
                )
                VerticalPDFReader(
                    state = pdfVerticalReaderState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Gray)
                )
            }
        }

        else -> {
            RecordImagePreview(filePaths = filePaths, onSelectUri = onUriSelected)
        }
    }
}

@Composable
fun RecordImagePreview(filePaths: List<Uri>, onSelectUri: (Uri) -> Unit) {
    var selectedUri by remember { mutableStateOf(filePaths.firstOrNull()) }

    LaunchedEffect(Unit) {
        filePaths.firstOrNull()?.let { it1 -> onSelectUri(it1) }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        selectedUri?.let {
            ImagePreview(
                uri = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(24.dp)
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp, start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(filePaths) {
                ImagePreview(
                    uri = it,
                    modifier = Modifier
                        .size(48.dp)
                        .border(
                            width = 1.dp,
                            color = EkaTheme.colors.outlineVariant
                        )
                        .clickable {
                            selectedUri = it
                            onSelectUri(it)
                        }
                )
            }
        }
    }
}