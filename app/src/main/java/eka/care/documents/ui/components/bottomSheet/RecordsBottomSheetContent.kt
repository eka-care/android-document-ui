package eka.care.documents.ui.components.bottomSheet

import androidx.compose.runtime.Composable
import eka.care.documents.ui.navigation.MedicalRecordsNavModel
import eka.care.documents.ui.state.UpsertRecordState
import eka.care.documents.ui.utility.DocumentBottomSheetType
import eka.care.documents.ui.utility.RecordsAction
import eka.care.documents.ui.viewmodel.RecordsViewModel

@Composable
fun RecordsBottomSheetContent(
    viewModel: RecordsViewModel,
    params: MedicalRecordsNavModel,
    caseId: String? = null,
    onClick: (String) -> Unit,
    onShare: () -> Unit,
    onEditDocument: () -> Unit,
    scanDocument: () -> Unit,
    pickPdf: () -> Unit,
    cameraLauncher: () -> Unit,
    pickImagesFromGallery: () -> Unit,
    onAssignCase: () -> Unit,
    owners: List<String>
) {
    when (viewModel.documentBottomSheetType) {
        DocumentBottomSheetType.DocumentUpload -> {
            RecordUploadBottomSheet(
                onClick = {
                    onClick(RecordsAction.ACTION_CLOSE_SHEET)
                    when (it) {
                        RecordsAction.ACTION_TAKE_PHOTO -> {
                            cameraLauncher.invoke()
                        }

                        RecordsAction.ACTION_SCAN_A_DOCUMENT -> {
                            scanDocument.invoke()
                        }

                        RecordsAction.ACTION_CHOOSE_FROM_GALLERY -> {
                            pickImagesFromGallery.invoke()
                        }

                        RecordsAction.ACTION_UPLOAD_PDF -> {
                            pickPdf.invoke()
                        }
                    }
                }
            )
        }

        DocumentBottomSheetType.DocumentOptions -> {
            RecordOptionsBottomSheet(
                onClick = {
                    onClick(RecordsAction.ACTION_CLOSE_SHEET)
                    when (it) {
                        RecordsAction.ACTION_EDIT_DOCUMENT -> {
                            onClick(RecordsAction.ACTION_OPEN_SHEET)
                            viewModel.documentBottomSheetType =
                                DocumentBottomSheetType.EnterFileDetails
                        }

                        RecordsAction.ACTION_SHARE_DOCUMENT -> {
                            onShare()
                        }

                        RecordsAction.ACTION_DELETE_RECORD -> {
                            onClick(RecordsAction.ACTION_OPEN_DELETE_DIALOG)
                        }

                        RecordsAction.ASSIGN_DOCUMENT_TO_CASE -> {
                            onAssignCase()
                        }
                    }
                }
            )
        }

        DocumentBottomSheetType.DocumentSort -> {
            RecordSortBottomSheet(
                selectedSort = viewModel.sortBy.value,
                onCloseClick = {
                    onClick(RecordsAction.ACTION_CLOSE_SHEET)
                },
                onClick = {
                    viewModel.sortBy.value = it
                    viewModel.fetchRecords(
                        owners = owners,
                        businessId = params.businessId,
                        caseId = caseId
                    )
                    onClick(RecordsAction.ACTION_CLOSE_SHEET)
                },
            )
        }

        DocumentBottomSheetType.EnterFileDetails -> {
            EnterDetailsBottomSheet(
                onClick = {
                    onClick(RecordsAction.ACTION_CLOSE_SHEET)
                    viewModel.updateRecordState(UpsertRecordState.NONE)
                    onEditDocument()
                },
                fileList = ArrayList(),
                caseId = caseId,
                paramsModel = params,
                viewModel = viewModel,
                editDocument = true
            )
        }

        DocumentBottomSheetType.TagsFilter -> {
            TagsBottomSheet(
                viewModel = viewModel,
                onTagsUpdated = {
                    viewModel.tags.value = it
                    viewModel.fetchRecords(
                        owners = owners,
                        businessId = params.businessId,
                        caseId = caseId
                    )
                }
            )
        }

        null -> {}
    }
}