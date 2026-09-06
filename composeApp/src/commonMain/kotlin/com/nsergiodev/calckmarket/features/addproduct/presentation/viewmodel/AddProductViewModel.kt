package com.nsergiodev.calckmarket.features.addproduct.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsergiodev.calckmarket.core.utils.getCurrentEpochMillis
import com.nsergiodev.calckmarket.features.addproduct.domain.model.MarketType
import com.nsergiodev.calckmarket.features.addproduct.domain.model.Product
import com.nsergiodev.calckmarket.features.addproduct.domain.usecase.DiscardDraftBuyUseCase
import com.nsergiodev.calckmarket.features.addproduct.domain.usecase.GetDraftBuyUseCase
import com.nsergiodev.calckmarket.features.addproduct.domain.usecase.SaveBuyUseCase
import com.nsergiodev.calckmarket.features.home.domain.model.Buy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddProductViewModel(
    private val saveBuyUseCase: SaveBuyUseCase,
    private val getDraftBuyUseCase: GetDraftBuyUseCase,
    private val discardDraftBuyUseCase: DiscardDraftBuyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AddProductUiState(draftId = "${getCurrentEpochMillis()}")
    )
    val uiState: StateFlow<AddProductUiState> = _uiState.asStateFlow()

    init {
        loadExistingDraftIfAny()
    }

    private fun loadExistingDraftIfAny() {
        viewModelScope.launch {
            val existingDraft = getDraftBuyUseCase().firstOrNull()
            if (existingDraft != null && !existingDraft.isCompleted) {
                _uiState.update {
                    it.copy(
                        draftId = existingDraft.id,
                        marketName = existingDraft.marketName,
                        selectedMarketType = existingDraft.marketType,
                        products = existingDraft.products,
                        totalAmount = existingDraft.totalAmount,
                        totalItems = existingDraft.productCount
                    )
                }
            }
        }
    }

    private fun autoSaveDraft() {
        val current = _uiState.value
        val draftBuy = Buy(
            id = current.draftId,
            marketName = current.marketName,
            dateEpochMillis = getCurrentEpochMillis(),
            products = current.products,
            paidAmount = 0.0,
            isCompleted = false
        )
        viewModelScope.launch {
            saveBuyUseCase(draftBuy)
        }
    }

    fun onMarketSelected(marketType: MarketType, customName: String? = null) {
        val name = customName ?: marketType.displayName
        _uiState.update {
            it.copy(
                marketName = name,
                selectedMarketType = marketType
            )
        }
        autoSaveDraft()
    }

    fun onAddProduct(name: String, price: Double, quantity: Double) {
        if (name.isBlank() || price <= 0.0 || quantity <= 0.0) return

        val newProduct = Product(
            id = "${getCurrentEpochMillis()}_${_uiState.value.products.size}",
            name = name.trim(),
            price = price,
            quantity = quantity,
            total = price * quantity
        )

        _uiState.update { current ->
            val updatedProducts = listOf(newProduct) + current.products
            current.copy(
                products = updatedProducts,
                totalAmount = updatedProducts.sumOf { it.total },
                totalItems = updatedProducts.size
            )
        }
        autoSaveDraft()
    }

    fun onIncrementQuantity(productId: String) {
        _uiState.update { current ->
            val updated = current.products.map { product ->
                if (product.id == productId) {
                    val newQty = product.quantity + 1.0
                    product.copy(quantity = newQty, total = newQty * product.price)
                } else product
            }
            current.copy(
                products = updated,
                totalAmount = updated.sumOf { it.total }
            )
        }
        autoSaveDraft()
    }

    fun onDecrementQuantity(productId: String) {
        _uiState.update { current ->
            val updated = current.products.mapNotNull { product ->
                if (product.id == productId) {
                    val newQty = product.quantity - 1.0
                    if (newQty <= 0.0) null
                    else product.copy(quantity = newQty, total = newQty * product.price)
                } else product
            }
            current.copy(
                products = updated,
                totalAmount = updated.sumOf { it.total },
                totalItems = updated.size
            )
        }
        autoSaveDraft()
    }

    fun onUpdateProduct(updatedProduct: Product) {
        _uiState.update { current ->
            val updated = current.products.map {
                if (it.id == updatedProduct.id) {
                    updatedProduct.copy(total = updatedProduct.price * updatedProduct.quantity)
                } else it
            }
            current.copy(
                products = updated,
                totalAmount = updated.sumOf { it.total },
                editingProduct = null
            )
        }
        autoSaveDraft()
    }

    fun onRemoveProduct(productId: String) {
        _uiState.update { current ->
            val updated = current.products.filter { it.id != productId }
            current.copy(
                products = updated,
                totalAmount = updated.sumOf { it.total },
                totalItems = updated.size
            )
        }
        autoSaveDraft()
    }

    fun onSetEditingProduct(product: Product?) {
        _uiState.update { it.copy(editingProduct = product) }
    }

    fun onShowPayDifferenceSheet(show: Boolean) {
        if (show && _uiState.value.products.isEmpty()) {
            _uiState.update { it.copy(showEmptyProductsAlert = true) }
            return
        }
        _uiState.update { it.copy(showPayDifferenceSheet = show) }
    }

    fun onShowEmptyProductsAlert(show: Boolean) {
        _uiState.update { it.copy(showEmptyProductsAlert = show) }
    }

    fun onDiscardDraft() {
        viewModelScope.launch {
            discardDraftBuyUseCase()
            _uiState.update {
                AddProductUiState(draftId = "${getCurrentEpochMillis()}")
            }
        }
    }

    fun onCompletePurchase(paidAmount: Double) {
        val currentState = _uiState.value
        if (currentState.products.isEmpty()) return

        val buyId = currentState.draftId.ifBlank { "${getCurrentEpochMillis()}" }
        val buy = Buy(
            id = buyId,
            marketName = currentState.marketName,
            dateEpochMillis = getCurrentEpochMillis(),
            products = currentState.products,
            paidAmount = paidAmount,
            isCompleted = true
        )

        viewModelScope.launch {
            saveBuyUseCase(buy)
            _uiState.update {
                it.copy(
                    showPayDifferenceSheet = false,
                    isSavedSuccess = true,
                    savedBuyId = buyId
                )
            }
        }
    }
}
