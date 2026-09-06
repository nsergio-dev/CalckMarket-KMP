package com.nsergiodev.calckmarket.features.home.presentation.viewmodel

import com.nsergiodev.calckmarket.features.home.domain.model.Buy

data class HomeUiState(
    val buys: List<Buy> = emptyList(),
    val totalSpent: Double = 0.0,
    val totalPurchases: Int = 0,
    val averagePerPurchase: Double = 0.0,
    val isLoading: Boolean = false,
    val selectedBuyForDetails: Buy? = null
)
