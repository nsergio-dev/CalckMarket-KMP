package com.nsergiodev.calckmarket.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsergiodev.calckmarket.features.home.domain.model.Buy
import com.nsergiodev.calckmarket.features.home.domain.usecase.DeleteBuyUseCase
import com.nsergiodev.calckmarket.features.home.domain.usecase.DiscardInProgressBuyUseCase
import com.nsergiodev.calckmarket.features.home.domain.usecase.GetBuysUseCase
import com.nsergiodev.calckmarket.features.home.domain.usecase.GetInProgressBuyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getBuysUseCase: GetBuysUseCase,
    private val getInProgressBuyUseCase: GetInProgressBuyUseCase,
    private val discardInProgressBuyUseCase: DiscardInProgressBuyUseCase,
    private val deleteBuyUseCase: DeleteBuyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeBuys()
        observeInProgressBuy()
    }

    private fun observeBuys() {
        _uiState.update { it.copy(isLoading = true) }
        getBuysUseCase()
            .onEach { buysList ->
                val totalSpent = buysList.sumOf { it.totalAmount }
                val totalPurchases = buysList.size
                val avg = if (totalPurchases > 0) totalSpent / totalPurchases else 0.0

                _uiState.update {
                    it.copy(
                        buys = buysList,
                        totalSpent = totalSpent,
                        totalPurchases = totalPurchases,
                        averagePerPurchase = avg,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeInProgressBuy() {
        getInProgressBuyUseCase()
            .onEach { draftBuy ->
                _uiState.update { it.copy(inProgressBuy = draftBuy) }
            }
            .launchIn(viewModelScope)
    }

    fun onNewPurchaseClicked(onNavigateToNew: () -> Unit) {
        if (_uiState.value.inProgressBuy != null) {
            _uiState.update { it.copy(showDraftChoiceDialog = true) }
        } else {
            onNavigateToNew()
        }
    }

    fun onDismissDraftDialog() {
        _uiState.update { it.copy(showDraftChoiceDialog = false) }
    }

    fun onDiscardDraftAndStartNew(onNavigateToNew: () -> Unit) {
        viewModelScope.launch {
            discardInProgressBuyUseCase()
            _uiState.update { it.copy(showDraftChoiceDialog = false) }
            onNavigateToNew()
        }
    }

    fun onDiscardDraft() {
        viewModelScope.launch {
            discardInProgressBuyUseCase()
            _uiState.update { it.copy(showDraftChoiceDialog = false) }
        }
    }

    fun onSelectBuy(buy: Buy?) {
        _uiState.update { it.copy(selectedBuyForDetails = buy) }
    }

    fun onDeleteBuy(id: String) {
        viewModelScope.launch {
            deleteBuyUseCase(id)
        }
    }
}
