package eka.care.documents.ui.model

data class RecordCase(
    val title: String,
    val recordCount: Int,
    val date: String,
    val iconRes: Int? = null,
)
