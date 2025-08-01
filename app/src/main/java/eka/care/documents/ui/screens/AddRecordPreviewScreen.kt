package eka.care.records.ui.presentation.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState
import eka.care.doctor.records.R
import eka.care.doctor.theme.color.DarwinTouchNeutral0
import eka.care.doctor.theme.color.DarwinTouchNeutral100
import eka.care.doctor.theme.color.DarwinTouchNeutral1000
import eka.care.doctor.theme.color.DarwinTouchPrimaryBgLight
import eka.care.doctor.typography.touchLabelBold
import eka.care.doctor.ui.molecule.IconButtonWrapper
import eka.care.doctor.ui.organism.AppBar
import eka.care.documents.ui.components.bottomSheet.EnterDetailsBottomSheet
import eka.care.documents.ui.navigation.AddRecordPreviewNavModel
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.records.ui.presentation.viewmodel.RecordsViewModel
import eka.care.records.ui.utility.RecordsUtility
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.io.IOException

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddRecordPreviewScreen(
    navData: AddRecordPreviewNavModel,
    onBackClick: () -> Unit,
    onUploadSuccess: () -> Unit
) {
    val context = LocalContext.current
    val filesPreviewList = arrayListOf<File>()
    val recordsViewModel: RecordsViewModel = koinViewModel()
    val pdfUriString = navData.pdfUriString
    val imageUriString = navData.imageUris

    val imageUris = imageUriString?.split(",") ?: emptyList()

    if (imageUris.isNotEmpty()) {
        for (uriString in imageUris) {
            val imageUri = uriString.toUri()
            val cameraPic = RecordsUtility.loadFromUri(context, imageUri)
            cameraPic?.let {
                val file = getFileFromUri(context, imageUri)
                file?.let { f -> filesPreviewList.add(f) }
            }
        }
    }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = false
    )
    val scope = rememberCoroutineScope()

    val openSheet = {
        scope.launch {
            sheetState.show()
        }
    }
    val closeSheet = {
        scope.launch {
            sheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            EnterDetailsBottomSheet(
                onClick = {
                    closeSheet()
                    onUploadSuccess()
                },
                viewModel = recordsViewModel,
                fileList = if (pdfUriString != null) arrayListOf(
                    uriToFile(
                        context,
                        pdfUriString.toUri()
                    )
                ) else filesPreviewList,
                paramsModel = MedicalRecordsNavModel(
                    ownerId = navData.ownerId,
                    filterId = navData.filterId
                ),
                editDocument = false
            )
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        content = {
            PreviewComponent(
                onClick = {
                    openSheet()
                },
                onBackClick = {
                    onBackClick()
                },
                filePreviewList = filesPreviewList,
                pdfUriString = pdfUriString ?: ""
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewComponent(
    onBackClick: () -> Unit,
    onClick: () -> Unit,
    filePreviewList: List<File>,
    pdfUriString: String
) {
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
                        icon = eka.care.doctor.icons.R.drawable.ic_arrow_left_regular,
                        contentDescription = "Back",
                    )
                }
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .background(DarwinTouchNeutral100)
                    .padding(it)
            ) {
                if (pdfUriString.isEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 32.dp, vertical = 16.dp)
                    ) {
                        items(filePreviewList) { file ->
                            val bitmap = BitmapFactory.decodeFile(file.path)
                                ?.let { fixImageOrientation(it, file.path) }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = bitmap,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.padding(80.dp))
                        }
                    }
                } else {
                    val pdfVerticalReaderState = rememberVerticalPdfReaderState(
                        resource = ResourceType.Local(pdfUriString.toUri()),
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
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(DarwinTouchNeutral0)
                    .wrapContentSize()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                CircularImageComponent(
                    image = R.drawable.ic_files_selection_done_tick,
                    modifier = Modifier,
                    onClick = onClick,
                    action = "Upload"
                )
            }
        }
    )
}

fun fixImageOrientation(bitmap: Bitmap, filePath: String): Bitmap {
    try {
        val exif = ExifInterface(filePath)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        val matrix = Matrix()

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
            else -> return bitmap
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bitmap
}


@Composable
fun CircularImageComponent(image: Int, modifier: Modifier, onClick: () -> Unit, action: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(50))
                .clickable {
                    onClick()
                }
                .background(DarwinTouchPrimaryBgLight)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = "",
                modifier = Modifier.size(32.dp)
            )
        }
        Text(
            text = action,
            style = touchLabelBold,
            color = DarwinTouchNeutral1000,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

private fun uriToFile(context: Context, uri: Uri): File {
    val fileName = "temp_pdf_${System.currentTimeMillis()}.pdf"
    val file = File(context.cacheDir, fileName)

    return try {
        val inputStream = context.applicationContext.contentResolver.openInputStream(uri)
            ?: throw IOException("Unable to open input stream for URI: $uri")
        val outputStream = file.outputStream()
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        file
    } catch (e: Exception) {
        e.printStackTrace()
        throw IOException("Failed to convert URI to file: ${e.message}", e)
    }
}

private fun getFileFromUri(context: Context, uri: Uri): File? {
    return try {
        val inputStream =
            context.applicationContext.contentResolver.openInputStream(uri)
        val tempFile = File(
            context.applicationContext.cacheDir,
            "temp_image_${System.currentTimeMillis()}.jpg"
        )
        tempFile.outputStream().use { output ->
            inputStream?.copyTo(output)
        }
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}