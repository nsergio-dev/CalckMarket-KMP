package com.nsergiodev.calckmarket.presentation.screens.detail.models

data class PurchaseSummaryUiState(
    val marketName: String,
    val dateText: String,
    val total: String,
    val paid: String,
    val difference: String,
    val differenceState: SummaryDifferenceState,
    val products: List<SummaryProductUi>
)
