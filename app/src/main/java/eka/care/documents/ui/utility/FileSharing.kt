package eka.care.documents.ui.utility

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File


class FileSharing {
    companion object {
        fun shareFiles(context: Context, filePaths: List<String>) {
            try {
                if (filePaths.isEmpty()) {
                    Toast.makeText(context, "No files to share", Toast.LENGTH_SHORT).show()
                    return
                }

                val uris = kotlin.collections.ArrayList<Uri>()
                for (filePath in filePaths) {
                    val file = File(filePath)
                    if (!file.exists()) {
                        Toast.makeText(
                            context,
                            "File does not exist: $filePath",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        continue
                    }

                    // Generate a URI for each file
                    val uri = FileProvider.getUriForFile(
                        context,
                        eka.care.records.client.utils.Document.getConfiguration().provider,
                        file
                    )
                    uris.add(uri)
                }

                if (uris.isEmpty()) {
                    Toast.makeText(context, "No valid files to share", Toast.LENGTH_SHORT).show()
                    return
                }

                // Determine MIME type
                val mimeType = when {
                    filePaths.all { it.endsWith(".pdf", true) } -> "application/pdf"
                    filePaths.all {
                        it.endsWith(".jpg", true) || it.endsWith(
                            ".jpeg",
                            true
                        )
                    } -> "image/jpeg"

                    else -> "*/*"
                }

                // Use ACTION_SEND_MULTIPLE for multiple files
                val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    type = mimeType
                    putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
                }

                // Start share activity
                context.startActivity(Intent.createChooser(intent, "Share files via"))
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error sharing files: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        fun handleFileDownload(
            context: Context,
            uri: Uri? = null
        ) {
            try {
                if (uri == null) {
                    Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show()
                    return
                }

                val file = File(uri.path ?: "")

                if (file.extension == "pdf") {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri, "application/pdf")
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    context.startActivity(intent)
                    return
                }

                if (!file.exists()) {
                    Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show()
                    return
                }

                downloadFile(context = context, file = file)

            } catch (ex: Exception) {
                ex.printStackTrace()
                Toast.makeText(context, "Error downloading file: ${ex.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        fun downloadFile(context: Context, file: File) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
                        put(MediaStore.MediaColumns.MIME_TYPE, file.getMimeType())
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    }
                    val destFileUri = context.contentResolver.insert(
                        MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                        contentValues
                    )
                    if (destFileUri == null) {
                        Log.d("FileDebug", "Failed to create file URI")
                        return
                    }
                    context.contentResolver.openOutputStream(destFileUri)?.use { outputStream ->
                        file.inputStream().use { inputStream ->
                            inputStream.copyTo(outputStream)
                            Log.d("FileDebug", "File successfully copied to $destFileUri")
                        }
                    }
                } else {
                    val path =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            .toString()
                    val dir = File(path)
                    if (!dir.exists()) dir.mkdirs()
                    val destFile = File(dir, file.name)
                    file.copyTo(destFile, overwrite = true)
                    Log.d("FileDebug", "File successfully copied to $destFile")
                }
                Toast.makeText(context, "File downloaded successfully", Toast.LENGTH_SHORT).show()
            } catch (ex: Exception) {
                ex.printStackTrace()
                Toast.makeText(context, "Error downloading file: ${ex.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        fun File.getMimeType(): String? =
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(this.extension)
    }
}