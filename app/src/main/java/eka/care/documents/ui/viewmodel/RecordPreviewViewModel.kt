package eka.care.documents.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import eka.care.documents.ui.components.Filter
import eka.care.documents.ui.components.LabParamResult
import eka.care.documents.ui.components.SmartViewTab
import eka.care.documents.ui.state.DocumentPreviewState
import eka.care.records.client.utils.Records
import eka.care.records.data.remote.dto.response.SmartReport
import eka.care.records.data.remote.dto.response.SmartReportField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecordPreviewViewModel(val app: Application) : AndroidViewModel(app) {

    private val recordsManager = Records.getInstance(
        context = app.applicationContext,
        token = ""
    )

    private val _selectedTab = MutableStateFlow(SmartViewTab.SMART_REPORT)
    fun updateSelectedTab(newTab: SmartViewTab) {
        _selectedTab.value = newTab
    }

    private val _selectedFilter = MutableStateFlow(Filter.ALL)
    val selectedFilter: StateFlow<Filter> = _selectedFilter

    private val _filteredSmartReport = MutableStateFlow<List<SmartReportField>>(emptyList())
    val filteredSmartReport: StateFlow<List<SmartReportField>> = _filteredSmartReport

    private val _document = MutableStateFlow<DocumentPreviewState>(DocumentPreviewState.Loading)
    val document: StateFlow<DocumentPreviewState> = _document

    fun updateFilter(filter: Filter, smartReport: SmartReport?) {
        _selectedFilter.value = filter
        _filteredSmartReport.value = getFilteredSmartReport(smartReport)
    }

    fun initializeReports(smartReport: SmartReport?) {
        _filteredSmartReport.value = getFilteredSmartReport(smartReport)
    }

    fun getFilteredSmartReport(smartReport: SmartReport?): List<SmartReportField> {
        return when (_selectedFilter.value) {
            Filter.ALL -> smartReport?.verified.orEmpty()
            Filter.OUT_OF_RANGE -> smartReport?.verified?.filter { field ->
                val resultEnum = LabParamResult.entries.find { it.value == field.resultId }
                resultEnum != LabParamResult.NORMAL && resultEnum != LabParamResult.NO_INTERPRETATION_DONE
            } ?: emptyList()
        }
    }


    fun getDocument(id: String) {
        viewModelScope.launch {
            try {
                val record = recordsManager.getRecordDetailsById(id = id)
                if (record == null) {
                    _document.value = DocumentPreviewState.Error("Something went wrong!")
                    return@launch
                }
                if (record.files.isEmpty()) {
                    _document.value = DocumentPreviewState.Error("No files found!")
                    return@launch
                }

                _document.value = DocumentPreviewState.Success(record)
            } catch (ex: Exception) {
                _document.value =
                    DocumentPreviewState.Error(ex.localizedMessage ?: "Something went wrong!")
            }
        }
    }
}