package com.nsergiodev.calckmarket.features.addproduct.presentation.viewmodel

import com.nsergiodev.calckmarket.features.addproduct.domain.model.MarketType
import com.nsergiodev.calckmarket.features.addproduct.domain.model.Product

data class AddProductUiState(
    val marketName: String = MarketType.D1.displayName,
    val selectedMarketType: MarketType = MarketType.D1,
    val products: List<Product> = emptyList(),
    val totalAmount: Double = 0.0,
    val totalItems: Int = 0,
    val isFormValid: Boolean = false,
    val editingProduct: Product? = null,
    val showPayDifferenceSheet: Boolean = false,
    val showEmptyProductsAlert: Boolean = false,
    val isSavedSuccess: Boolean = false,
    val savedBuyId: String? = null
)
