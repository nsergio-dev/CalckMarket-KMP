package com.nsergiodev.calckmarket.features.detail.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsergiodev.calckmarket.features.detail.domain.usecase.GetBuyDetailUseCase
import com.nsergiodev.calckmarket.features.home.domain.model.Buy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class PurchaseDetailUiState(
    val buy: Buy? = null,
    val isLoading: Boolean = true,
    val showTemplateSavedMessage: Boolean = false,
    val showShareMessage: Boolean = false
)

class PurchaseDetailViewModel(
    private val buyId: String,
    private val getBuyDetailUseCase: GetBuyDetailUseCase = GetBuyDetailUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PurchaseDetailUiState())
    val uiState: StateFlow<PurchaseDetailUiState> = _uiState.asStateFlow()

    init {
        loadBuy()
    }

    private fun loadBuy() {
        getBuyDetailUseCase(buyId)
            .onEach { buyItem ->
                _uiState.update {
                    it.copy(
                        buy = buyItem,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSaveTemplate() {
        _uiState.update { it.copy(showTemplateSavedMessage = true) }
    }

    fun onSharePdf() {
        _uiState.update { it.copy(showShareMessage = true) }
    }

    fun onDismissFeedback() {
        _uiState.update {
            it.copy(
                showTemplateSavedMessage = false,
                showShareMessage = false
            )
        }
    }
}
